package main.java.Controller.Api.SaleRecord;


import main.java.Database.SaleRecordDataController;
import main.java.Model.SaleRecord;
import main.java.Model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GetSaleRecordController extends HttpServlet {

    private final String[] GetSaleRecordMessage = {"Get SaleRecord Successfully", "Without Permission"};
    private final Logger logger = LogManager.getLogger(GetSaleRecordController.class);

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        User user = (User) request.getAttribute("User");

        if (user != null) {
            try {
                List<SaleRecord> saleRecords = new ArrayList<>();

                ResultSet resultSet = SaleRecordDataController.getSaleRecord();
                SaleRecordDataController.closeConnection();

                while (resultSet.next()) {
                    SaleRecord saleRecord = new SaleRecord(resultSet.getInt("saleRecordID"), 
                            resultSet.getFloat("totalPrice"), resultSet.getString("data"));
                    saleRecords.add(saleRecord);
                }

                request.setAttribute("SaleRecords", saleRecords);
                request.setAttribute("Code", 0);
                request.setAttribute("Message", GetSaleRecordMessage[0]);
            } catch (SQLException sqlException) {
                logger.error(sqlException.getMessage());
            }
        } else {
            request.setAttribute("Code", 1);
            request.setAttribute("Message", GetSaleRecordMessage[1]);
        }

        request.getRequestDispatcher("").forward(request, response);
    }

}