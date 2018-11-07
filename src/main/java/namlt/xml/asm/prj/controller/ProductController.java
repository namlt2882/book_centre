package namlt.xml.asm.prj.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import namlt.xml.asm.prj.model.Book;
import namlt.xml.asm.prj.model.BookList;
import namlt.xml.asm.prj.service.BookDetailCrawlingService;
import namlt.xml.asm.prj.service.BookService;
import namlt.xml.asm.prj.service.BookUtilityService;

@Path("/product")
public class ProductController extends BaseController {

    public static final int MAX_ITEM_PER_PAGE = 10;

    @POST
    @Path("/list")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.TEXT_PLAIN)
    public Response addAllBookInPage(BookList bookList) {
        List<Book> listToAdd = bookList.getBook();
        if (listToAdd == null) {
            listToAdd = new ArrayList<>();
        }
        BookService bookService = new BookService();
        int successCase = 0;
        for (Book book : listToAdd) {
            try {
                Book rs = bookService.add(book);
                //edit status of book in cache
                BookDetailCrawlingService.editBookFromCache(rs.getUrl(), b -> {
                    BookUtilityService.copyBook(rs, b);
                    b.setExistedInDb(true);
                });
                successCase++;
            } catch (Exception ex) {
                System.out.println("[Error] Fail to insert book: [id=" + book.getId() + ",title='" + book.getTitle() + "']");
                Logger.getLogger(ProductController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        String message = successCase + "";
        return Response.ok(message).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_XML)
    public Response addNewBook(Book b) {
        BookService bookService = new BookService();
        Book rs;
        try {
            rs = bookService.add(b);
        } catch (Exception ex) {
            return Response.status(404).entity(ex.getMessage()).build();
        }
        if (rs != null) {
            //edit status of book in cache
            BookDetailCrawlingService.editBookFromCache(rs.getUrl(), book -> {
                BookUtilityService.copyBook(rs, book);
                book.setExistedInDb(true);
            });
            return Response.ok(b.getId()).build();
        } else {
            return Response.status(404).build();
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_XML)
    public Response updateBook(Book b) {
        BookService bookService = new BookService();
        try {
            bookService.update(b);
        } catch (Exception ex) {
            ex.printStackTrace();
            return Response.status(404).entity(ex.getMessage()).build();
        }
        //edit status of book in cache
        BookDetailCrawlingService.editBookFromCache(b.getUrl(), book -> {
            BookUtilityService.copyBook(b, book);
            book.setExistedInDb(true);
        });
        return Response.ok(b.getId()).build();
    }

    @POST
    @Path("/{id}/SetActive")
    public Response setActiveBook(@PathParam("id") String id) {
        try {
            boolean rs = new BookService().setActiveBook(id);
            if (!rs) {
                throw new Exception();
            }
            return Response.ok().build();
        } catch (Exception e) {
            return Response.status(404).build();
        }
    }

    @POST
    @Path("/{id}/SetDisable")
    public Response setDisableBook(@PathParam("id") String id) {
        try {
            boolean rs = new BookService().setDisableBook(id);
            if (!rs) {
                throw new Exception();
            }
            return Response.ok().build();
        } catch (Exception e) {
            return Response.status(404).build();
        }
    }

    @POST
    @Path("/{id}/SetOutOfStock")
    public Response setOutOfStockBook(@PathParam("id") String id) {
        try {
            boolean rs = new BookService().setOutOfStockBook(id);
            if (!rs) {
                throw new Exception();
            }
            return Response.ok().build();
        } catch (Exception e) {
            return Response.status(404).build();
        }
    }

}
