package namlt.xml.asm.prj.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import namlt.xml.asm.prj.common.UserCommon;
import namlt.xml.asm.prj.model.User;
import namlt.xml.asm.prj.service.UserService;

@Path("/auth")
public class AuthenticationController extends BaseController {

    public AuthenticationController() {
    }

    @Context
    private HttpServletRequest request;

    @POST
    @Path("/Login")
    public Response login(@FormParam("username") String username,
            @FormParam("password") String password,
            @FormParam("redirect_page") String redirectPage) {
        String host = getCurrentHost();
        String redirectUrl = "Home.jsp";

        UserService userService = new UserService();
        User user = userService.login(username, password);
        if (user != null) {
            //login successfully
            //create new session
            HttpSession session = request.getSession(true);
            session.setAttribute("USERNAME", user.getUsername());
            session.setAttribute("USER", user);
            //set redirect url
            if (user.getRole() == UserCommon.ROLE_ADMIN) {
                redirectUrl = "admin/Home.jsp";
            }
        } else {
            //login fail
            return Response.status(401).build();
        }
        if (redirectPage != null) {
            redirectUrl = redirectPage;
        }
        redirectUrl = host + '/' + redirectUrl;
        return Response.ok(redirectUrl).build();
    }

    @POST
    @Path("/Register")
    public Response register(@FormParam("username") String username,
            @FormParam("password") String password,
            @FormParam("address") String address,
            @FormParam("phone") String phone,
            @FormParam("name") String name) {
        User user = new User(username, password);
        user.setAddress(address);
        user.setPhone(phone);
        user.setName(name);
        User newUser = null;
        newUser = new UserService().register(user);
        if (newUser == null) {
            return Response.status(404).build();
        }
        return Response.ok().build();
    }

    @POST
    @Path("/Logout")
    public Response logout() {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return Response.status(401).build();
        } else {
            session.invalidate();
            return Response.ok().build();
        }
    }
}
