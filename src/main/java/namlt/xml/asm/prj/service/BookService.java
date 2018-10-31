/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package namlt.xml.asm.prj.service;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import namlt.xml.asm.prj.common.BookCommon;
import namlt.xml.asm.prj.model.Book;
import namlt.xml.asm.prj.repository.BookRepository;

/**
 *
 * @author ADMIN
 */
public class BookService implements BookCommon {

    public Book add(Book b) {
        if (b == null) {
            return null;
        }
        try {
            b.setStatus(STATUS_NEW);
            b.setQuantity(0);
            b.setInsertDate(new Date());
            return new BookRepository().insert(b);
        } catch (Exception ex) {
            Logger.getLogger(BookService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
