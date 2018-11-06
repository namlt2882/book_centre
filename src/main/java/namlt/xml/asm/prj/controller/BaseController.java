package namlt.xml.asm.prj.controller;

import java.net.URI;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

public class BaseController {

    @Context
    protected UriInfo uriInfo;

    @Context
    protected HttpServletRequest request;
    
    public BaseController() {
    }

    protected String getCurrentHost() {
        URI baseUri = uriInfo.getBaseUri();
        String host = baseUri.getScheme() + "://" + baseUri.getHost() + ':' + baseUri.getPort();
        return host;
    }
}
