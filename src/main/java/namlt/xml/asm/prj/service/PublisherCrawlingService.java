/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package namlt.xml.asm.prj.service;

import java.util.ArrayList;
import java.util.List;
import namlt.xml.asm.prj.crawler.BookCrawler;
import namlt.xml.asm.prj.crawler.NhaNamCrawler;
import namlt.xml.asm.prj.crawler.NxbTreCrawler;
import namlt.xml.asm.prj.model.Book;

/**
 *
 * @author ADMIN
 */
public class PublisherCrawlingService {
    
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
