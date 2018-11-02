package namlt.xml.asm.prj.crawler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import namlt.xml.asm.prj.model.Book;
import namlt.xml.asm.prj.parser.NestedTagResolver;
import static namlt.xml.asm.prj.utils.InternetUtils.crawl;

public interface BookCrawler {

    Book crawlBookPage(String url);

    List<String> crawlNewBookUrls(String url);

    List<String> crawlNextNewBookUrls(int start, int time);

    String generateId(Book b);

    List<String> search(String s);

    default String getHtmlSource(String url) throws IOException {
        String htmlSource = crawl(url);
        if (htmlSource != null) {
            StringBuilder sb = new StringBuilder();
            NestedTagResolver.formatNestedTag(htmlSource, "html").forEach(sb::append);
            htmlSource = sb.toString();
        }
        return htmlSource;
    }

    default List<Book> crawlBookPages(List<String> urls) {
        List<Book> rs = new ArrayList<>();
        urls.parallelStream().map(s -> crawlBookPage(s))
                .filter(b -> {
                    if (!BookCrawler.validateData(b)) {
                        return false;
                    }
                    return true;
                })
                .forEach(b -> {
                    synchronized (rs) {
                        rs.add(b);
                    }
                });
        return rs;
    }

    static boolean validateData(Book b) {
        //check not null
        if (b == null) {
            return false;
        }
        //check title and price
        if (b.getTitle() == null || b.getPrice() == null) {
            return false;
        }
        return true;
    }

}
