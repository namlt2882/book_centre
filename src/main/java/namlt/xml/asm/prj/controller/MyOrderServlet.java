package namlt.xml.asm.prj.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import namlt.xml.asm.prj.model.Order;
import namlt.xml.asm.prj.model.mapping.CollectionWrapper;
import namlt.xml.asm.prj.service.OrderService;
import namlt.xml.asm.prj.utils.MarshallerUtils;

@WebServlet(name = "MyOrderServlet", urlPatterns = {"/my_order"})
public class MyOrderServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String username = (String) session.getAttribute("USERNAME");
        List<Order> orders = new ArrayList<>();
        if (username != null) {
            try {
                orders = new OrderService().getOrderByCustomerId(username);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
//        try {
//            CollectionWrapper<Order> wrapper = new CollectionWrapper<>(orders);
//            JAXBElement jaxbElement = MarshallerUtils.newJaxbElement("orders", CollectionWrapper.class, wrapper);
//            String xmlData = MarshallerUtils.marshall(jaxbElement, CollectionWrapper.class, Order.class);
//            request.setAttribute("orderXmlData", xmlData);
//        } catch (JAXBException ex) {
//            ex.printStackTrace();
//        }
        request.setAttribute("orders", orders);
    }

}
