package namlt.xml.asm.prj.controller;

import java.net.URI;
import java.util.List;
import javax.enterprise.inject.Default;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import namlt.xml.asm.prj.model.Book;
import namlt.xml.asm.prj.service.BookService;
import namlt.xml.asm.prj.service.PublisherCrawlingService;

@Path("/product")
public class ProductController {

    @Context
    private UriInfo uriInfo;

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
        BookService bookService = new BookService();
        int successCase = 0;
        for (Book book : listToAdd) {
            Book rs = bookService.add(book);
            if (rs != null) {
                //remove item from crawling cache
                PublisherCrawlingService.removeBookFromCache(book.getId());
                successCase++;
            } else {
                System.out.println("[Error] Fail to insert book: [id=" + book.getId() + ",title='" + book.getTitle() + "']");
            }
        }
        uriBuilder.queryParam("msg", "Thêm thành công " + successCase + " sản phẩm, " + (listToAdd.size() - successCase) + " sản phẩm thất bại!");
        return Response.seeOther(uriBuilder.build()).build();
    }

    @POST
    public Response addNewBook(@FormParam("cache_key") String cacheKey, @FormParam("id") String id,
            @FormParam("author") String author, @FormParam("translator") String translator,
            @FormParam("size") String size, @FormParam("page_number") String pageNumber,
            @FormParam("price") Double price, @FormParam("description") String description,
            @FormParam("title") String title, @FormParam("image_url") String imageUrl,
            @FormParam("url") String url, @FormParam("isbn") String isbn) {
        //build uri to redirect
        String redirectUrl = "/admin/Publisher.jsp";
        redirectUrl = getCurrentHost() + redirectUrl;
        UriBuilder uriBuilder = UriBuilder.fromUri(redirectUrl).queryParam("cache_key", cacheKey);

        Book b = new Book(id, title, author, price);
        b.setTranslator(translator);
        b.setPageSize(size);
        try {
            b.setPageNumber(Integer.parseInt(pageNumber));
        } catch (Exception e) {
        }
        b.setDescription(description);
        b.setImageUrl(imageUrl);
        b.setUrl(url);
        b.setIsbn(isbn);

        BookService bookService = new BookService();
        Book rs = bookService.add(b);
        if (rs != null) {
            //remove item from crawling cache
            PublisherCrawlingService.removeBookFromCache(id);
            uriBuilder.queryParam("msg", "THÀNH CÔNG! Thêm sách mới với id = '" + id + "'!");
        } else {
            uriBuilder.queryParam("msg", "THẤT BẠI! Thử lại nhé!");
        }
        return Response.seeOther(uriBuilder.build()).build();
    }

    private String getCurrentHost() {
        URI baseUri = uriInfo.getBaseUri();
        String host = baseUri.getScheme() + "://" + baseUri.getHost() + ':' + baseUri.getPort();
        return host;
    }
}
