/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package namlt.xml.asm.prj.crawler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import namlt.xml.asm.prj.crawler.model.Book;
import namlt.xml.asm.prj.parser.NestedTagResolver;
import static namlt.xml.asm.prj.utils.InternetUtils.crawl;

/**
 *
 * @author ADMIN
 */
public class NhaNamCrawler {

    public static void main(String[] args) {
        NhaNamCrawler crawler = new NhaNamCrawler();
        crawler.crawBookPage("http://nhanam.vn/sach/sach-mau-la-hoa-si-trang-phuc");
    }

    public Book crawBookPage(String url) {
        Book book = null;
        try {
            String htmlSource = crawl(url);
            if (htmlSource != null) {
                StringBuilder sb = new StringBuilder();
                NestedTagResolver.formatNestedTag(htmlSource).forEach(sb::append);
                htmlSource = sb.toString();
            }
            writeToFile(htmlSource);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return book;
    }

    public static void writeToFile(String xmlSource) throws UnsupportedEncodingException, FileNotFoundException, IOException {
        ClassLoader classLoader = NhaNamCrawler.class.getClassLoader();
        String fileLocation = classLoader.getResource("crawler/schema/output.xml").getFile();
        File file = new File(URLDecoder.decode(fileLocation, "UTF-8"));
        FileOutputStream fw = new FileOutputStream(file);
        StringReader reader = new StringReader(xmlSource);
        int a;
        while ((a = reader.read()) != -1) {
            fw.write((char) a);
        }
        fw.flush();
        fw.close();
    }
}
