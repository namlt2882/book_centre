package namlt.xml.asm.prj.controller;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.naming.NamingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import namlt.xml.asm.prj.model.Book;
import namlt.xml.asm.prj.service.BookService;
import namlt.xml.asm.prj.service.PublisherCrawlingService;

@Path("/product")
public class ProductController extends BaseController {

    public static final int MAX_ITEM_PER_PAGE = 10;

    @POST
    @Path("/list")
    public Response addAllBookInPage(@FormParam("publisher") String publisher,
            @FormParam("search") String search, @FormParam("cache_key") String cacheKey,
            @FormParam("page") Integer page) {
        String redirectUrl = "/admin/Publisher.jsp";
        redirectUrl = getCurrentHost() + redirectUrl;
        UriBuilder uriBuilder = UriBuilder.fromUri(redirectUrl).queryParam("cache_key", cacheKey);
        List<Book> listToAdd;
        if (publisher == null) {
            publisher = "nxb-nhanam";
        }
        if (page == null) {
            page = 1;
        }
        PublisherCrawlingService crawlingService = new PublisherCrawlingService();
        if (search != null) {
            listToAdd = crawlingService.search(publisher, search);
        } else {
            listToAdd = crawlingService.getNewBook(publisher, page - 1, page);
        }
        listToAdd = listToAdd.parallelStream().filter(b -> !b.isExistedInDb()).collect(Collectors.toList());
        BookService bookService = new BookService();
        int successCase = 0;
        for (Book book : listToAdd) {
            Book rs = null;
            try {
                rs = bookService.add(book);
            } catch (Exception ex) {
                Logger.getLogger(ProductController.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (rs != null) {
                //edit status of book in cache
                PublisherCrawlingService.editBookFromCache(rs.getUrl(), b -> {
                    b.setExistedInDb(true);
                });
                successCase++;
            } else {
                System.out.println("[Error] Fail to insert book: [id=" + book.getId() + ",title='" + book.getTitle() + "']");
            }
        }
        uriBuilder.queryParam("msg", "Thêm thành công " + successCase + " sản phẩm, " + (listToAdd.size() - successCase) + " sản phẩm thất bại!");
        return Response.seeOther(uriBuilder.build()).build();
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
            PublisherCrawlingService.editBookFromCache(rs.getUrl(), book -> {
                BookService.copyBook(rs, book);
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
        PublisherCrawlingService.editBookFromCache(b.getUrl(), book -> {
            BookService.copyBook(b, book);
            book.setExistedInDb(true);
        });
        return Response.ok(b.getId()).build();
    }

}
