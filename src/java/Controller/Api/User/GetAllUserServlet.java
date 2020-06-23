package Controller.Api.User;

import Database.UserDataController;
import Model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "GetAllUserServlet")
public class GetAllUserServlet extends HttpServlet {
    private static final String[] Message = {"Get Info Successfully", "Without Permission"};
    private final Logger logger = LogManager.getLogger(getClass());

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("User");

        if (currentUser != null) {
            if ("staff".equals(currentUser.getPosition())) {
                request.setAttribute("Code", 1);
                request.setAttribute("Message", Message[1]);
            } else {
                try {
                    List<User> users = null;
                    UserDataController userDataController = new UserDataController();
                    users = userDataController.queryAll();

                    request.setAttribute("UsersInfo", users);
                    request.setAttribute("Code", 0);
                    request.setAttribute("Message", Message[0]);
                } catch (SQLException sqlException) {
                    logger.error(sqlException.getMessage());
                } catch (NamingException namingException) {
                    logger.error(namingException.getMessage());
                } catch (IllegalAccessException illegalAccessException) {
                    logger.error(illegalAccessException.getMessage());
                } catch (ClassNotFoundException classNotFoundException) {
                    logger.error(classNotFoundException.getMessage());
                } catch (InstantiationException instantiationException) {
                    logger.error(instantiationException.getMessage());
                }
            }
        }
    }
}