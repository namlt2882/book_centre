/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package namlt.xml.asm.prj.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "books")
public class BookList {

    @XmlElement(name = "book")
    private List<Book> books;

    public BookList() {
    }

    public BookList(List<Book> c) {
        books = c;
    }

}
