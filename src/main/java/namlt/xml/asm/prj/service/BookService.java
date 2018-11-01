package namlt.xml.asm.prj.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import namlt.xml.asm.prj.common.BookCommon;
import namlt.xml.asm.prj.model.Book;
import namlt.xml.asm.prj.repository.BookRepository;

public class BookService implements BookCommon {

    public Book add(Book b) {
        if (b == null) {
            return null;
        }
        try {
            b.setStatus(STATUS_ACTIVE);
            b.setQuantity(0);
            b.setInsertDate(new Date());
            return new BookRepository().insert(b);
        } catch (Exception ex) {
            Logger.getLogger(BookService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Book find(String id) {
        try {
            return new BookRepository().get(id);
        } catch (Exception ex) {
            Logger.getLogger(BookService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public List<Book> getNew(Integer startAt, Integer nextRow) {
        List<Book> rs = null;
        if (startAt == null || nextRow == null) {
            startAt = 0;
            nextRow = 10;
        }
        try {
            rs = new BookRepository().getNew(startAt, nextRow);
        } catch (Exception ex) {
            Logger.getLogger(BookService.class.getName()).log(Level.SEVERE, null, ex);
            rs = new ArrayList<>();
        }
        return rs;
    }

    public List<Book> getOutOfStock(Integer startAt, Integer nextRow) {
        List<Book> rs = null;
        if (startAt == null || nextRow == null) {
            startAt = 0;
            nextRow = 10;
        }
        try {
            rs = new BookRepository().getOutOfStock(startAt, nextRow);
        } catch (Exception ex) {
            Logger.getLogger(BookService.class.getName()).log(Level.SEVERE, null, ex);
            rs = new ArrayList<>();
        }
        return rs;
    }

    public List<Book> getDisable(Integer startAt, Integer nextRow) {
        List<Book> rs = null;
        if (startAt == null || nextRow == null) {
            startAt = 0;
            nextRow = 10;
        }
        try {
            rs = new BookRepository().getDisable(startAt, nextRow);
        } catch (Exception ex) {
            Logger.getLogger(BookService.class.getName()).log(Level.SEVERE, null, ex);
            rs = new ArrayList<>();
        }
        return rs;
    }

    public int count() {
        try {
            return new BookRepository().count();
        } catch (Exception ex) {
            Logger.getLogger(BookService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public int countActive() {
        try {
            return new BookRepository().countActive();
        } catch (Exception ex) {
            Logger.getLogger(BookService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public int countOutOfStock() {
        try {
            return new BookRepository().countOutOfStock();
        } catch (Exception ex) {
            Logger.getLogger(BookService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public int countDisable() {
        try {
            return new BookRepository().countDisable();
        } catch (Exception ex) {
            Logger.getLogger(BookService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
}
