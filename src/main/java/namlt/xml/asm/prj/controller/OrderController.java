package namlt.xml.asm.prj.controller;

import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import namlt.xml.asm.prj.model.Order;
import namlt.xml.asm.prj.service.OrderService;

@Path("/order")
public class OrderController extends BaseController {

    @POST
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.TEXT_XML)
    public Response checkout(Order order) {
        HttpSession session = request.getSession(false);
        String username;
        if (session != null && (username = (String) session.getAttribute("USERNAME")) != null) {
            order.setCustomerId(username);
            try {
                Order newOrder = new OrderService().addOrder(order);
                return Response.ok(newOrder).build();
            } catch (Exception ex) {
                ex.printStackTrace();
                return Response.status(404).entity(ex.getMessage()).build();
            }
        } else {
            return Response.status(401).build();
        }
    }

    @POST
    @Path("/{orderId}/cancel")
    public Response cancelOrder(@PathParam("orderId") Integer orderId) {
        try {
            OrderService service = new OrderService();
            boolean success = service.cancelOrder(orderId);
            if (success) {
                return Response.ok().build();
            } else {
                throw new Exception();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(404).entity(e.getMessage()).build();
        }
    }
}
