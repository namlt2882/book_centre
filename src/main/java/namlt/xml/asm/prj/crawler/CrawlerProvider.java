package namlt.xml.asm.prj.crawler;

import namlt.xml.asm.prj.utils.InternetUtils;

public class CrawlerProvider {

    public static BookCrawler identifyCrawlerByUrl(String url) {
        String host = InternetUtils.identifyHost(url);
        BookCrawler crawler = getCrawler(host);
        return crawler;
    }

    public static BookCrawler getCrawler(String publisher) {
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
