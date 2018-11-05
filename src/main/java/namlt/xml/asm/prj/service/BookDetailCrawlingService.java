package namlt.xml.asm.prj.service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import jersey.repackaged.com.google.common.cache.CacheBuilder;
import jersey.repackaged.com.google.common.cache.CacheLoader;
import jersey.repackaged.com.google.common.cache.LoadingCache;
import namlt.xml.asm.prj.crawler.BookCrawler;
import namlt.xml.asm.prj.model.Book;
import static namlt.xml.asm.prj.crawler.CrawlerProvider.identifyCrawlerByUrl;

public class BookDetailCrawlingService {

    private static LoadingCache<String, Book> bookDetailCache;

    static {
        bookDetailCache = CacheBuilder.newBuilder()
                .maximumSize(500)
                .expireAfterWrite(30, TimeUnit.MINUTES)
                .build(new BookDetailCacheLoader());
    }

    public static Book getBook(String url) {
        try {
            return bookDetailCache.apply(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Book> getBooks(List<String> urls) {
        return urls.parallelStream()
                .map(u -> getBook(u))
                .filter(b -> b != null)
                .collect(Collectors.toList());
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

    private static class BookDetailCacheLoader extends CacheLoader<String, Book> {

        private BookUtilityService bookUtilityService = new BookUtilityService();

        @Override
        public Book load(String url) throws Exception {
            BookCrawler crawler = identifyCrawlerByUrl(url);
            if (crawler == null) {
                throw new Exception("Not found crawler for url '" + url + "'");
            }
            System.out.println("[WARNING] [" + new Date() + "] Crawling book detail from '" + url + "'");
            Book rs = crawler.crawlBookPage(url);
            if (rs == null) {
                throw new Exception("[ERROR] [" + new Date() + "] Fail to parse book detail from url '" + url + "'");
            }
            boolean isExisted = bookUtilityService.isBookExisted(rs.getId());
            rs.setExistedInDb(isExisted);
            return rs;
        }

    }
}
