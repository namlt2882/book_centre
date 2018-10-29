package namlt.xml.asm.prj.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import namlt.xml.asm.prj.model.Book;
import namlt.xml.asm.prj.service.PublisherCrawlingService;

@WebServlet(name = "CrawlServlet", urlPatterns = {"/crawl"})
public class CrawlServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String publisher = "nxb-nhanam";
        String search = request.getParameter("search");
        String tmp = request.getParameter("publisher");
        PublisherCrawlingService crawlingService = new PublisherCrawlingService();
        List<Book> rs = null;
        if (tmp != null) {
            publisher = tmp;
        }
        if (search != null) {
            rs = crawlingService.search(publisher, search);
        } else {
            rs = crawlingService.getNewBook(publisher, 0, 1);
        }
        request.setAttribute("books", rs);
    }

}