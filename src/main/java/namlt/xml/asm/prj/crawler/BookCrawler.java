/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package namlt.xml.asm.prj.crawler;

import java.util.List;
import namlt.xml.asm.prj.model.Book;

/**
 *
 * @author ADMIN
 */
public interface BookCrawler {

    Book crawlBookPage(String url);

    List<String> crawlNewBookUrls(String url);

    List<Book> crawlNextNewBooks(int start, int time);

    String generateId(Book b);

    List<Book> search(String s);

    default boolean validateData(Book b) {
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
