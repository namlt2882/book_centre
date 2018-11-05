package namlt.xml.asm.prj.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import jersey.repackaged.com.google.common.cache.CacheBuilder;
import jersey.repackaged.com.google.common.cache.CacheLoader;
import jersey.repackaged.com.google.common.cache.LoadingCache;
import namlt.xml.asm.prj.crawler.BookCrawler;
import namlt.xml.asm.prj.model.Book;
import static namlt.xml.asm.prj.crawler.CrawlerProvider.getCrawler;

public class PublisherCrawlingService {

    private static LoadingCache<String, List<String>> pageCache;

    public static final String PREFIX_CRAWL_NEW = "crawl-new";
    public static final String PREFIX_CRAWL_SEARCH = "search";
    public static final String PREFIX_CRAWL_CATEGORY = "category";
    public static final int MAX_NEW_PAGE_QUANTITY = 20;

    static {
        pageCache = CacheBuilder.newBuilder()
                .maximumSize(100)
                .expireAfterWrite(15, TimeUnit.MINUTES)
                .build(new PageCacheLoader());

    }

    private String buildSearchCacheKey(String publisher, String search) {
        String key = PREFIX_CRAWL_SEARCH + "\n" + publisher + '\n' + search;
        return key;
    }

    private String buildNewBookUrlsCacheKey(String publisher, int start, int time) {
        String key = PREFIX_CRAWL_NEW + '\n' + publisher + '\n' + start + '\n' + time;
        return key;
    }

    public List<Book> getNewBook(String publisher, int start) {
        if (start < 0) {
            start = 0;
        }
        List<Book> rs = new ArrayList<>();
        int skipCounter = MAX_NEW_PAGE_QUANTITY * start;
        //skip to specific cache
        int i = 0;
        while (true) {
            String keyCode = buildNewBookUrlsCacheKey(publisher, i, (i + 1));
            i++;
            List<Book> cacheData = getFromCache(keyCode);
            int cacheDataSize = cacheData.size();
            if (cacheDataSize == 0) {
                return rs;
            }
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
            String keyCode = buildNewBookUrlsCacheKey(publisher, i, (i + 1));
            i++;
            List<Book> cacheData = getFromCache(keyCode);
            if (cacheData.size() == 0) {
                return rs;
            }
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

    private List<Book> getFromCache(String key) {
        List<Book> rs = null;
        try {
            List<String> urls = pageCache.apply(key);
            rs = BookDetailCrawlingService.getBooks(urls);
        } catch (Exception e) {
            System.out.println("[ERROR]: " + e.getMessage());
        }
        if (rs == null) {
            return new ArrayList<>();
        }
        return rs;
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

}
