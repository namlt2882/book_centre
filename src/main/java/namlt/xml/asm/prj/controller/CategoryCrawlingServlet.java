package namlt.xml.asm.prj.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;
import namlt.xml.asm.prj.model.Category;
import namlt.xml.asm.prj.model.CategoryList;
import namlt.xml.asm.prj.service.CategoryCrawlingService;
import namlt.xml.asm.prj.utils.MarshallerUtils;

@WebServlet(name = "CategoryCrawlingServlet", urlPatterns = {"/category_crawling_servlet"})
public class CategoryCrawlingServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String xmlData = "";
        List<Category> categories = CategoryCrawlingService.getAllCategories();
        if (categories == null) {
            categories = new ArrayList<>();
        }
        CategoryList categoryList = new CategoryList(categories);
        try {
            xmlData = MarshallerUtils.marshall(categoryList);
        } catch (JAXBException ex) {
            Logger.getLogger(CategoryCrawlingServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        request.setAttribute("crawlCategoryXmlData", xmlData);

    }

}
