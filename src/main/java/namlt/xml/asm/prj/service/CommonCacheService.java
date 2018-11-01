package namlt.xml.asm.prj.service;

import java.util.concurrent.TimeUnit;
import jersey.repackaged.com.google.common.cache.CacheBuilder;
import jersey.repackaged.com.google.common.cache.CacheLoader;
import jersey.repackaged.com.google.common.cache.LoadingCache;
import jersey.repackaged.com.google.common.cache.Weigher;
import namlt.xml.asm.prj.model.Book;

public class CommonCacheService {

    private static LoadingCache<String, Boolean> bookExistingCache;

    static {
        bookExistingCache = CacheBuilder.newBuilder()
                .weigher(new Weigher<String, Boolean>() {
                    @Override
                    public int weigh(String k, Boolean v) {
                        if (v) {
                            return 0;
                        } else {
                            return 1;
                        }
                    }
                }).maximumWeight(0)
                .expireAfterWrite(24, TimeUnit.HOURS)
                .build(new BookExistingCache());
    }

    public boolean isBookExisted(String id) {
        try {
            Boolean rs = bookExistingCache.apply(id);
            return rs;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static class BookExistingCache extends CacheLoader<String, Boolean> {

        @Override
        public Boolean load(String k) throws Exception {
            Book book = new BookService().get(k);
            if (book == null) {
                return false;
            } else {
                return true;
            }
        }

    }
}
