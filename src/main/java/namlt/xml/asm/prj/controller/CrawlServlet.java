package namlt.xml.asm.prj.controller;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;
import namlt.xml.asm.prj.model.Book;
import namlt.xml.asm.prj.model.BookList;
import namlt.xml.asm.prj.service.PublisherCrawlingService;
import namlt.xml.asm.prj.utils.MarshallerUtils;

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
        String xmlData = "";
        if (tmp != null) {
            publisher = tmp;
        }
        if (search != null) {
            rs = crawlingService.search(publisher, search);
        } else {
            String pageStr = request.getParameter("page");
            int page = 1;
            if (pageStr != null && !"".equals(pageStr.trim())) {
                try {
                    page = Integer.parseInt(pageStr);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (page <= 0) {
                page = 1;
            }
            request.setAttribute("page", page);
            rs = crawlingService.getNewBook(publisher, page - 1);
        }
        if (rs != null) {
            try {
                BookList bl = new BookList(rs);
                xmlData = MarshallerUtils.marshall(bl);
            } catch (JAXBException ex) {
                Logger.getLogger(CrawlServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        request.setAttribute("books", rs);
        request.setAttribute("xmlData", xmlData);
    }

}
