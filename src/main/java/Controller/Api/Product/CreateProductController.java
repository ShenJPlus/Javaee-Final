package main.java.Controller.Api.Product;

import main.java.Database.ProductDataController;
import main.java.Model.Product;
import main.java.Model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

public class CreateProductController extends HttpServlet {
    private static final String[] Message = {"Create Products Successfully", "Without Permission", "Empty Request", "Product Name Already Exist"};
    private final Logger logger = LogManager.getLogger(CreateProductController.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User currentUser = (User) session.getAttribute("currentUser");

        if (currentUser != null) {
            if ("staff".equals(currentUser.getPosition())) {
                req.setAttribute("Code", 1);
                req.setAttribute("Message", Message[1]);
            } else {
                if (req.getParameter("productName") != null && req.getParameter("productPrice") != null) {
                    Product product = new Product(req.getParameter("productName"), Integer.parseInt(req.getParameter("productPrice")));

                    try {
                        ProductDataController productDataController = new ProductDataController();
                        productDataController.add(product);

                        req.setAttribute("Code", 0);
                        req.setAttribute("Message", Message[0]);
                    } catch (SQLException sqlException) {
                        if (sqlException.getErrorCode() == 1062) {
                            req.setAttribute("Code", 3);
                            req.setAttribute("Message", Message[3]);
                        }
                        logger.error(sqlException.getMessage());
                    } catch (NamingException namingException) {
                        logger.error(namingException.getMessage());
                    } catch (IllegalAccessException illegalAccessException) {
                        logger.error(illegalAccessException.getMessage());
                    }
                } else {
                    req.setAttribute("Code", 2);
                    req.setAttribute("Message", Message[2]);
                }
            }
        } else {
            req.setAttribute("Code", 1);
            req.setAttribute("Message", Message[1]);
        }

        req.getRequestDispatcher("").forward(req, resp);
    }
}