package namlt.xml.asm.prj.service;

import namlt.xml.asm.prj.model.Book;

public class BookUtilityService {

    public static void copyBook(Book src, Book des) {
        des.setTitle(src.getTitle());
        des.setIsbn(src.getIsbn());
        des.setAuthor(src.getAuthor());
        des.setTranslator(src.getTranslator());
        des.setPageSize(src.getPageSize());
        des.setPageNumber(src.getPageNumber());
        des.setPrice(src.getPrice());
        des.setUrl(src.getUrl());
        des.setImageUrl(src.getImageUrl());
        des.setDescription(src.getDescription());
    }

    public boolean isBookExisted(String id) {
        Book book = new BookService().get(id);
        if (book == null) {
            return false;
        } else {
            return true;
        }
    }

}
