package namlt.xml.asm.prj.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import jersey.repackaged.com.google.common.cache.CacheBuilder;
import jersey.repackaged.com.google.common.cache.CacheLoader;
import jersey.repackaged.com.google.common.cache.LoadingCache;
import namlt.xml.asm.prj.crawler.BookCrawler;
import namlt.xml.asm.prj.model.Category;
import static namlt.xml.asm.prj.crawler.CrawlerProvider.getCrawler;

public class CategoryCrawlingService {

    private static LoadingCache<String, List<Category>> categoryCache;

    static {
        categoryCache = CacheBuilder.newBuilder()
                .expireAfterWrite(30, TimeUnit.MINUTES)
                .build(new CategoryCacheLoader());
    }

    public static List<Category> getAllCategories() {
        List<Category> rs = getNhaNamCategories();
        getTreCategories().forEach(c -> rs.add(c));
        return rs;
    }

    public static List<Category> getNhaNamCategories() {
        try {
            List<Category> rs = categoryCache.apply("nxb-nhanam");
            return rs;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public static List<Category> getTreCategories() {
        try {
            List<Category> rs = categoryCache.apply("nxb-tre");
            return rs;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    private static class CategoryCacheLoader extends CacheLoader<String, List<Category>> {

        @Override
        public List<Category> load(String publisher) throws Exception {
            BookCrawler crawler = getCrawler(publisher);
            if (crawler == null) {
                throw new Exception("Not found publisher:" + publisher);
            }
            List<Category> rs = crawler.crawlCategoryUrls();
            return rs;
        }

    }
}
