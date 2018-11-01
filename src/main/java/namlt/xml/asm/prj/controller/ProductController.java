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
        b.setPageNumber(Integer.parseInt(pageNumber));
        b.setDescription(description);
        b.setImageUrl(imageUrl);
        b.setUrl(url);
        b.setIsbn(isbn);

        BookService bookService = new BookService();
        Book rs = bookService.add(b);
        if (rs != null) {
            //remove item from crawling cache
            PublisherCrawlingService.removeBookFromCache(id);
            uriBuilder.queryParam("msg", "Success to insert new book with id = '" + id + "'!");
        } else {
            uriBuilder.queryParam("msg", "Fail to insert new book with id = '" + id + "'!");
        }
        return Response.seeOther(uriBuilder.build()).build();
    }

    private String getCurrentHost() {
        URI baseUri = uriInfo.getBaseUri();
        String host = baseUri.getScheme() + "://" + baseUri.getHost() + ':' + baseUri.getPort();
        return host;
    }
}
