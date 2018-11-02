package namlt.xml.asm.prj.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import jersey.repackaged.com.google.common.cache.CacheBuilder;
import jersey.repackaged.com.google.common.cache.CacheLoader;
import jersey.repackaged.com.google.common.cache.LoadingCache;
import namlt.xml.asm.prj.crawler.BookCrawler;
import namlt.xml.asm.prj.crawler.NhaNamCrawler;
import namlt.xml.asm.prj.crawler.NxbTreCrawler;
import namlt.xml.asm.prj.model.Book;
import namlt.xml.asm.prj.utils.InternetUtils;

public class PublisherCrawlingService {

    private static LoadingCache<String, List<String>> pageCache;
    private static LoadingCache<String, Book> bookDetailCache;
    public static final String PREFIX_CRAWL_NEW = "crawl-new";
    public static final String PREFIX_CRAWL_SEARCH = "search";
    public static final int MAX_NEW_PAGE_QUANTITY = 20;

    static {
        pageCache = CacheBuilder.newBuilder()
                .maximumSize(100)
                .expireAfterWrite(15, TimeUnit.MINUTES)
                .build(new PageCacheLoader());
        bookDetailCache = CacheBuilder.newBuilder()
                .maximumSize(500)
                .expireAfterWrite(30, TimeUnit.MINUTES)
                .build(new BookDetailCacheLoader());
    }

    public String buildSearchCacheKey(String publisher, String search) {
        String key = PREFIX_CRAWL_SEARCH + "\n" + publisher + '\n' + search;
        return key;
    }

    public String buildGetNewBookCacheKey(String publisher, int start, int time) {
        String key = "new\n" + publisher + '\n' + start + '\n' + time;
        return key;
    }

    private String newBookUrlsCacheKey(String publisher, int start, int time) {
        String key = PREFIX_CRAWL_NEW + '\n' + publisher + '\n' + start + '\n' + time;
        return key;
    }

    public static void removeBookFromCache(String url) {
        if (url == null || "".equals(url)) {
            return;
        }
        bookDetailCache.invalidate(url);
    }

    public static boolean editBookFromCache(String key, Consumer<Book> consumer) {
        Book book = bookDetailCache.getIfPresent(key);
        if (book != null) {
            consumer.accept(book);
            return true;
        }
        return false;
    }

    private List<Book> getBookFromUrls(List<String> url) {
        return url.parallelStream()
                .map(u -> {
                    try {
                        return bookDetailCache.apply(u);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .filter(b -> b != null)
                .collect(Collectors.toList());
    }

    public List<Book> getNewBook(String publisher, int start, int time) {
        if (start < 0) {
            start = 0;
        }
        List<Book> rs = new ArrayList<>();
        int skipCounter = MAX_NEW_PAGE_QUANTITY * start;
        //skip to specific cache
        int i = 0;
        while (true) {
            String keyCode = newBookUrlsCacheKey(publisher, i, (i + 1));
            i++;
            List<Book> cacheData = getFromCache(keyCode);
            int cacheDataSize = cacheData.size();
            if (skipCounter - cacheDataSize <= 0) {
                int position = skipCounter + 1;
                while (position <= cacheDataSize) {
                    if (rs.size() >= MAX_NEW_PAGE_QUANTITY) {
                        break;
                    }
                    rs.add(cacheData.get(position - 1));
                    position++;
                }
                break;
            } else {
                skipCounter = skipCounter - cacheDataSize;
            }
        }
        while (rs.size() < MAX_NEW_PAGE_QUANTITY) {
            String keyCode = newBookUrlsCacheKey(publisher, i, (i + 1));
            i++;
            List<Book> cacheData = getFromCache(keyCode);
            for (Book book : cacheData) {
                if (rs.size() >= MAX_NEW_PAGE_QUANTITY) {
                    break;
                }
                rs.add(book);
            }
        }
        return rs;
    }

    public List<Book> search(String publisher, String search) {
        return getFromCache(buildSearchCacheKey(publisher, search));
    }

    public List<Book> getFromCache(String key) {
        List<Book> rs = null;
        String[] keys = key.split("\n");
        if (keys != null && "new".equals(keys[0])) {
            return getNewBook(keys[1], Integer.parseInt(keys[2]), Integer.parseInt(keys[3]));
        }
        try {
            List<String> urls = pageCache.apply(key);
            rs = getBookFromUrls(urls);
        } catch (Exception e) {
            System.out.println("[ERROR]: " + e.getMessage());
        }
        if (rs == null) {
            return new ArrayList<>();
        }
        return rs;
    }

    private static class BookDetailCacheLoader extends CacheLoader<String, Book> {

        private CommonCacheService commonCacheService = new CommonCacheService();

        @Override
        public Book load(String url) throws Exception {
            String host = InternetUtils.identifyHost(url);
            BookCrawler crawler = getCrawler(host);
            if (crawler == null) {
                throw new Exception("Not found crawler for host '" + host + "'");
            }
            System.out.println("[WARNING] [" + new Date() + "] Crawling book detail from '" + url + "'");
            Book rs = crawler.crawlBookPage(url);
            if (rs == null) {
                throw new Exception("[ERROR] [" + new Date() + "] Fail to parse book detail from url '" + url + "'");
            }
            boolean isExisted = commonCacheService.isBookExisted(rs.getId());
            rs.setExistedInDb(isExisted);
            return rs;
        }

    }

    private static class PageCacheLoader extends CacheLoader<String, List<String>> {

        @Override
        public List<String> load(String k) throws Exception {
            String[] tmp = k.split("\n");
            List<String> rs = null;
            Date date = new Date();
            if (tmp != null) {
                switch (tmp[0]) {
                    case PREFIX_CRAWL_NEW:
                        System.out.println("[WARNNING] [" + date + "] Crawling new book urls from " + tmp[1]);
                        rs = getNewBook(tmp[1], Integer.parseInt(tmp[2]), Integer.parseInt(tmp[3]));
                        break;
                    case "search":
                        System.out.println("[WARNNING] [" + date + "] Crawling book urls according to search '"
                                + tmp[2] + "' result from " + tmp[1]);
                        rs = search(tmp[1], tmp[2]);
                        break;
                }
            }
            if (rs == null) {
                rs = new ArrayList<>();
            }
            return rs;
        }

        public List<String> search(String publisher, String search) {
            List<String> rs = null;
            BookCrawler crawler = getCrawler(publisher);
            if (crawler != null) {
                rs = crawler.search(search);
            } else {
                rs = new ArrayList<>();
            }
            return rs;
        }

        public List<String> getNewBook(String publisher, int start, int time) {
            List<String> rs = null;
            BookCrawler crawler = getCrawler(publisher);
            if (crawler != null) {
                rs = crawler.crawlNextNewBookUrls(start, time);
            } else {
                rs = new ArrayList<>();
            }
            return rs;
        }

    }

    private static BookCrawler getCrawler(String publisher) {
        switch (publisher) {
            case "nxb-nhanam":
                return new NhaNamCrawler();
            case "nhanam.com.vn":
                return new NhaNamCrawler();
            case "nxb-tre":
                return new NxbTreCrawler();
            case "nxbtre.com.vn":
                return new NxbTreCrawler();
            case "www.nxbtre.com.vn":
                return new NxbTreCrawler();
        }
        return null;
    }
}
