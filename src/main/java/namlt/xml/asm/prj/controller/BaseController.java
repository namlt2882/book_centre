package namlt.xml.asm.prj.controller;

import java.net.URI;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

public class BaseController {

    @Context
    protected UriInfo uriInfo;

    public BaseController() {
    }

    protected String getCurrentHost() {
        URI baseUri = uriInfo.getBaseUri();
        String host = baseUri.getScheme() + "://" + baseUri.getHost() + ':' + baseUri.getPort();
        return host;
    }
}
