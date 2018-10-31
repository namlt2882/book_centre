package namlt.xml.asm.prj.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import jersey.repackaged.com.google.common.cache.CacheBuilder;
import jersey.repackaged.com.google.common.cache.CacheLoader;
import jersey.repackaged.com.google.common.cache.LoadingCache;
import namlt.xml.asm.prj.crawler.BookCrawler;
import namlt.xml.asm.prj.crawler.NhaNamCrawler;
import namlt.xml.asm.prj.crawler.NxbTreCrawler;
import namlt.xml.asm.prj.model.Book;

public class PublisherCrawlingService {

    private static LoadingCache<String, List<Book>> bookCache;

    static {
        bookCache = CacheBuilder.newBuilder()
                .maximumSize(100)
                .expireAfterWrite(15, TimeUnit.MINUTES)
                .build(new BookCacheLoader());
    }

    public String buildSearchCacheKey(String publisher, String search) {
        String key = "search\n" + publisher + '\n' + search;
        return key;
    }

    public String buildGetNewBookCacheKey(String publisher, int start, int time) {
        String key = "new\n" + publisher + '\n' + start + '\n' + time;
        return key;
    }

    public static void removeBookFromCache(String id) {
        if (id == null || "".equals(id)) {
            return;
        }
        bookCache.asMap().values().parallelStream().forEach(lb -> {
            lb.removeIf(b -> id.equals(b.getId()));
        });
    }

    public List<Book> getNewBook(String publisher, int start, int time) {
        return getFromCache(buildGetNewBookCacheKey(publisher, start, time));
    }

    public List<Book> search(String publisher, String search) {
        return getFromCache(buildSearchCacheKey(publisher, search));
    }

    public List<Book> getFromCacheIfPresent(String cacheKey) {
        List<Book> rs = null;
        try {
            rs = bookCache.getIfPresent(cacheKey);
        } catch (Exception e) {
            System.out.println("[ERROR]: " + e.getMessage());
        }
        return rs;
    }

    public List<Book> getFromCache(String key) {
        List<Book> rs = null;
        try {
            rs = bookCache.apply(key);
        } catch (Exception e) {
            System.out.println("[ERROR]: " + e.getMessage());
        }
        if (rs == null) {
            return new ArrayList<>();
        }
        return rs;
    }

    private static class BookCacheLoader extends CacheLoader<String, List<Book>> {

        private CommonCacheService commonCacheService = new CommonCacheService();

        @Override
        public List<Book> load(String k) throws Exception {
            String[] tmp = k.split("\n");
            List<Book> rs = null;
            if (tmp != null) {
                switch (tmp[0]) {
                    case "new":
                        System.out.println("[WARNNING] Start to get new book from " + tmp[1]);
                        rs = getNewBook(tmp[1], Integer.parseInt(tmp[2]), Integer.parseInt(tmp[3]));
                        break;
                    case "search":
                        System.out.println("[WARNNING] Start to search '" + tmp[2] + "' from " + tmp[1]);
                        rs = search(tmp[1], tmp[2]);
                        break;
                }
            }
            if (rs == null) {
                rs = new ArrayList<>();
            } else if (rs.size() > 0) {
                List<String> existedId = rs.parallelStream()
                        .map(b -> {
                            boolean isExisted = commonCacheService.isBookExisted(b.getId());
                            if (isExisted) {
                                return b.getId();
                            } else {
                                return null;
                            }
                        })
                        .filter(id -> id != null).collect(Collectors.toList());
                for (String string : existedId) {
                    rs.removeIf(b -> string.equals(b.getId()));
                }
            }
            return rs;
        }

        public List<Book> search(String publisher, String search) {
            List<Book> rs = null;
            BookCrawler crawler = getCrawler(publisher);
            if (crawler != null) {
                rs = crawler.search(search);
            } else {
                rs = new ArrayList<>();
            }
            return rs;
        }

        public List<Book> getNewBook(String publisher, int start, int time) {
            List<Book> rs = null;
            BookCrawler crawler = getCrawler(publisher);
            if (crawler != null) {
                rs = crawler.crawlNextNewBooks(start, time);
            } else {
                rs = new ArrayList<>();
            }
            return rs;
        }

        private BookCrawler getCrawler(String publisher) {
            switch (publisher) {
                case "nxb-nhanam":
                    return new NhaNamCrawler();
                case "nxb-tre":
                    return new NxbTreCrawler();
            }
            return null;
        }
    }
}
