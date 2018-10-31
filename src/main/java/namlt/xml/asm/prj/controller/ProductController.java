package namlt.xml.asm.prj.controller;

import java.net.URI;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import namlt.xml.asm.prj.service.PublisherCrawlingService;

@Path("/product")
public class ProductController {
    
    @Context
    private UriInfo uriInfo;
    
    @POST
    public Response addNewBook(@FormParam("cache_key") String cacheKey, @FormParam("id") String id,
            @FormParam("author") String author, @FormParam("translator") String translator,
            @FormParam("size") String size, @FormParam("page_number") String pageNumber,
            @FormParam("price") Double price, @FormParam("description") String description) {
        //build uri to redirect
        String url = "/admin/Publisher.jsp";
        url = getCurrentHost() + url;
        URI uri = UriBuilder.fromUri(url).queryParam("cache_key", cacheKey).build();

        //remove item from crawling cache
        PublisherCrawlingService.removeBookFromCache(id);
        return Response.seeOther(uri).build();
    }
    
    private String getCurrentHost() {
        URI baseUri = uriInfo.getBaseUri();
        String host = baseUri.getScheme() + "://" + baseUri.getHost() + ':' + baseUri.getPort();
        return host;
    }
}
