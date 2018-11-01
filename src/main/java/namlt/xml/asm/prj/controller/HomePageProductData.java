package namlt.xml.asm.prj.controller;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static namlt.xml.asm.prj.controller.ProductController.MAX_ITEM_PER_PAGE;
import namlt.xml.asm.prj.model.Book;
import namlt.xml.asm.prj.service.BookService;

@WebServlet(name = "HomePageProductData", urlPatterns = {"/home_page_product_data"})
public class HomePageProductData extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Integer page = null;
        String tmp = request.getParameter("page");
        if (tmp != null) {
            try {
                page = Integer.parseInt(tmp);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (page == null) {
            page = 1;
        }
        if (page <= 0) {
            page = 1;
        }
        Integer startAt = MAX_ITEM_PER_PAGE * (page - 1);
        Integer nextRow = MAX_ITEM_PER_PAGE;
        List<Book> rs = null;
        int maxPage = 0;
        double var = 0;
        BookService bookService = new BookService();
        rs = bookService.getNew(startAt, nextRow);
        var = Math.ceil(bookService.countActive() * 1.0 / MAX_ITEM_PER_PAGE);
        maxPage = (int) var;
        request.setAttribute("books", rs);
        request.setAttribute("page", page);
        request.setAttribute("pageQuantity", maxPage);
    }
}
