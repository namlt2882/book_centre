package namlt.xml.asm.prj.crawler;

import namlt.xml.asm.prj.parser.BaseParser;
import namlt.xml.asm.prj.parser.BoundReachedException;
import namlt.xml.asm.prj.parser.ParserHelper;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import static javax.xml.stream.XMLStreamConstants.START_ELEMENT;
import namlt.xml.asm.prj.model.Book;
import namlt.xml.asm.prj.model.Category;
import static namlt.xml.asm.prj.utils.CommonUtils.parseInt;
import static namlt.xml.asm.prj.utils.CommonUtils.parseDouble;

public class NxbTreCrawler extends BaseParser implements BookCrawler {

    private XMLInputFactory inputFactory = XMLInputFactory.newFactory();
    public static final String HOME_PAGE = "https://www.nxbtre.com.vn/";

    public NxbTreCrawler() {
        inputFactory.setProperty(
                XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, false);
        inputFactory.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, false);
    }

    public static void main(String[] args) {
        NxbTreCrawler crawler = new NxbTreCrawler();
//        Book book = crawler.crawlBookPage("https://www.nxbtre.com.vn/sach/24269.html");
//        System.out.println(book);
//        List<String> books = crawler.crawlNextNewBookUrls(2, 5);
//        List<String> books = crawler.search("Hoàng tử bé");
//        crawler.crawlBookPages(books).forEach(System.out::println);
//        List<Category> categories = crawler.crawlCategoryUrls();
//        categories.forEach(System.out::println);
        List<String> books = crawler.crawlNextCategoryBookUrls("https://www.nxbtre.com.vn/tu-sach/chinh-tri/", 0, 5);
        books.stream().map(str -> crawler.crawlBookPage(str)).forEach(System.out::println);
    }

    @Override
    public Book crawlBookPage(String url) {
        Book book = null;
        ValueIdentifier identifier = new Book().getIdentifier();
        try {
            String htmlSource = getHtmlSource(url);
            XMLStreamReader reader = inputFactory.createXMLStreamReader(new StringReader(htmlSource));
            ParserHelper fragmentParser = new ParserHelper(reader);
            ParserHelper detailParser = new ParserHelper(reader);
            int event;
            while (reader.hasNext()) {
                event = reader.next();
                if (event == START_ELEMENT) {
                    if (isTag("div", reader) && equalClass(reader, null, "autBigImg")) {
                        fragmentParser.mark();
                        try {
                            fragmentParser.skipTo("img");
                            //in case book has no image
                            book = (book == null) ? new Book(url) : book;
                            String imageUrl = reader.getAttributeValue("", "src");
                            book.setImageUrl("https://www.nxbtre.com.vn" + imageUrl);
                        } catch (BoundReachedException e) {
                        }
                    } else if (isTag("div", reader) && equalClass(reader, null, "aut-detail")) {
                        book = (book == null) ? new Book(url) : book;
                        //read title
                        fragmentParser.mark();
                        try {
                            fragmentParser.skipToCharacter();
                            String title = fragmentParser.readTextInside();
                            book.setTitle(title != null ? title.trim() : null);
                        } catch (BoundReachedException e) {
                            //end fragment
                        }
                    } else if (isTag("ul", reader) && equalClass(reader, null, "itemDetail-cat")) {
                        Map<String, String> valueMap = new HashMap<>();
                        StringBuilder sb = new StringBuilder();
                        try {
                            while (true) {
                                String key = null;
                                String value = null;
                                //skip to new fragment detail
                                try {
                                    fragmentParser.skipTo("li");
                                } catch (BoundReachedException e) {
                                    break;
                                }
                                try {
                                    detailParser.mark();
                                    //get the key
                                    detailParser.skipToCharacter();
                                    key = detailParser.readTextInside();

                                    //get the value
                                    detailParser.skipToCharacter();
                                    sb = new StringBuilder();
                                    detailParser.skipToBound(sb);
                                } catch (BoundReachedException e) {
                                    //do nothing
                                } finally {
                                    //end fragment detail
                                    fragmentParser.addCounter(-1);
                                }
                                value = sb.toString();
                                identifier.indentify(key != null ? key.trim() : null,
                                        value != null ? value.trim() : null);
                            }
                        } catch (Exception e) {
                        }
                        Map<String, String> values = identifier.values();
                        String author = values.get("AUTHOR");
                        String translator = values.get("TRANSLATOR");
                        Integer pageNumber = parseInt(values.get("PAGE_NUMBER")).orElse(null);
                        String isbn = values.get("ISBN");
                        Double price = parseDouble(values.get("PRICE")).orElse(null);

                        book.setAuthor(author != null ? author.replace("\n", "") : null);
                        book.setTranslator(translator != null ? translator.replace("\n", "") : null);
                        book.setPageSize(values.get("SIZE"));
                        //process page number here
                        book.setPageNumber(pageNumber);
                        book.setIsbn(isbn != null ? isbn.replace("\n", "").replace(" ", "") : null);
                        //process price here
                        book.setPrice(price);
                        book.setDescription(values.get("DESCRIPTION"));
                        break;
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println("[ERROR]: " + ex.getMessage());
        }
        if (book != null) {
            //generate id
            String id = generateId(book);
            if (id == null) {
                return null;
            } else {
                book.setId(id);
            }
        }
        return book;
    }

    @Override
    public List<String> crawlBookUrls(String url) {
        if (url == null) {
            return new ArrayList<>();
        }
        List<String> urls = new ArrayList<>();
        try {
            String htmlSource = getHtmlSource(url);
            XMLStreamReader reader = inputFactory.createXMLStreamReader(new StringReader(htmlSource));
            ParserHelper fragmentParser = new ParserHelper(reader);
            ParserHelper.writeToFile(htmlSource);
            int event;
            String link;
            while (reader.hasNext()) {
                event = reader.next();
                if (event == START_ELEMENT) {
                    if (isTag("div", reader) && equalClasses(reader, null, "bBox", "book-item")) {
                        fragmentParser.mark();
                        fragmentParser.skipTo("a");
                        link = reader.getAttributeValue("", "href");
                        if (link != null && !"".equals((link = link.trim()))) {
                            urls.add("https://www.nxbtre.com.vn" + link.replace("xem-them", "sach"));
                        }
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println("[ERROR]: " + ex.getMessage());
        }
        return urls;
    }

    @Override
    public List<String> crawlNextNewBookUrls(int start, int time) {
        String tmp = "https://www.nxbtre.com.vn/tu-sach/trang-";
        List<String> urls = new ArrayList<>();
        if (start < 0) {
            start = 0;
        }
        for (int i = start; i < time; i++) {
            String url = tmp + (i + 1) + '/';
            crawlBookUrls(url).forEach(s -> urls.add(s));
        }
        return urls;
    }

    @Override
    public String generateId(Book b) {
        String url = b.getUrl().replace("https://www.nxbtre.com.vn/sach/", "");
        Integer id = parseInt(url).orElse(null);
        if (id == null) {
            return null;
        } else {
            String newId = "nxbtre-" + id;
            return newId;
        }
    }

    @Override
    public List<String> search(String s) {
        String tmp = null;
        try {
            tmp = "https://www.nxbtre.com.vn/tim-kiem?q=" + URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(NxbTreCrawler.class.getName()).log(Level.SEVERE, null, ex);
        }
        List<String> urls = new ArrayList<>();
        crawlBookUrls(tmp).forEach(u -> urls.add(u));
        return urls;
    }

    @Override
    public List<Category> crawlCategoryUrls() {
        List<Category> rs = new ArrayList<>();
        try {
            String htmlSource = getHtmlSource(HOME_PAGE);
            XMLStreamReader reader = inputFactory.createXMLStreamReader(new StringReader(htmlSource));
            ParserHelper fragmentParser = new ParserHelper(reader);
            ParserHelper detailParser = new ParserHelper(reader);
            ParserHelper.writeToFile(htmlSource);
            int eventType;
            while (reader.hasNext()) {
                eventType = reader.next();
                if (eventType == START_ELEMENT) {
                    if (isTag("ul", reader) && equalClasses(reader, "", "navbar")) {
                        fragmentParser.mark();
                        boolean found = false;
                        //skip to specific li tag
                        while (true) {
                            try {
                                fragmentParser.skipToWithClassName("li", "drops");
                            } catch (BoundReachedException e) {
                                e.printStackTrace();
                                break;
                            }
                            try {
                                detailParser.mark();
                                detailParser.skipTo("a");
                                String href = reader.getAttributeValue("", "href");
                                if ("/tu-sach/".equals(href)) {
                                    found = true;
                                    break;
                                }
                                detailParser.skipToBound(null);
                            } catch (BoundReachedException e) {
                                e.printStackTrace();
                            } finally {
                                fragmentParser.addCounter(-1);
                            }
                        }
                        //parse detail if found=true
                        if (found) {
                            detailParser.skipToWithClassName("div", "mega-row");
                            fragmentParser.mark();
                            String name, url;
                            Category category;
                            while (true) {
                                try {
                                    fragmentParser.skipTo("li");
                                } catch (BoundReachedException e) {
                                    break;
                                }
                                try {
                                    detailParser.mark();
                                    detailParser.skipTo("a");
                                    url = reader.getAttributeValue("", "href");
                                    detailParser.skipToCharacter();
                                    name = detailParser.readTextInside();
                                    category = new Category(name.trim(), "https://www.nxbtre.com.vn" + url);
                                    rs.add(category);
                                    detailParser.skipToBound(null);
                                } catch (BoundReachedException e) {
                                } finally {
                                    fragmentParser.addCounter(-1);
                                }
                            }
                        }
                        break;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("[ERROR]: " + e.getMessage());
        }
        return rs;
    }

    @Override
    public List<String> crawlNextCategoryBookUrls(String categoryUrl, int start, int time) {
        categoryUrl += "trang-";
        List<String> urls = new ArrayList<>();
        if (start < 0) {
            start = 0;
        }
        for (int i = start; i < time; i++) {
            String url = categoryUrl + (i + 1) + '/';
            crawlBookUrls(url).forEach(s -> urls.add(s));
        }
        return urls;
    }

}
