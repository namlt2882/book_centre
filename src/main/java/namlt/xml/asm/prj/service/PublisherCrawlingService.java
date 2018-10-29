package namlt.xml.asm.prj.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
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
                .expireAfterWrite(5, TimeUnit.MINUTES)
                .build(new BookCacheLoader());
    }

    public List<Book> getNewBook(String publisher, int start, int time) {
        String key = "new\n" + publisher + '\n' + start + '\n' + time;
        return getFromCache(key);
    }

    public List<Book> search(String publisher, String search) {
        String key = "search\n" + publisher + '\n' + search;
        return getFromCache(key);
    }

    private List<Book> getFromCache(String key) {
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

        @Override
        public List<Book> load(String k) throws Exception {
            String[] tmp = k.split("\n");
            if (tmp != null) {
                switch (tmp[0]) {
                    case "new":
                        System.out.println("[WARNNING] Start to get new book from " + tmp[1]);
                        return getNewBook(tmp[1], Integer.parseInt(tmp[2]), Integer.parseInt(tmp[3]));
                    case "search":
                        System.out.println("[WARNNING] Start to search '" + tmp[2] + "' from " + tmp[1]);
                        return search(tmp[1], tmp[2]);
                }
            }
            return new ArrayList<>();
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
