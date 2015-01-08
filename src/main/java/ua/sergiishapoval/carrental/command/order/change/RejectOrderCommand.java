package ua.sergiishapoval.carrental.command.order.change;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.sergiishapoval.carrental.command.Command;
import ua.sergiishapoval.carrental.dao.DaoFactory;
import ua.sergiishapoval.carrental.dao.DaoOrder;
import ua.sergiishapoval.carrental.model.Order;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by Сергей on 27.12.2014.
 */
public class RejectOrderCommand implements Command {

    private static final Logger logger = LoggerFactory.getLogger(RejectOrderCommand.class);

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        RequestDispatcher requestDispatcher = null;
        request.getSession().removeAttribute("error");
        try {
            int orderId = Integer.parseInt(request.getParameter("id"));
            String reason =request.getParameter("reason");
            Order orderChosen = null;
            DaoOrder daoOrder = null;
            try {
                daoOrder = DaoFactory.getDaoOrder();
                boolean isUpdated = daoOrder.changeOrderReason(orderId, reason);
                if (isUpdated) {
                    orderChosen = daoOrder.getOrderDataByOrderId(orderId);
                    request.getSession().setAttribute("order", orderChosen);
                } else {
                    request.getSession().setAttribute("error", "DATABASE_PROBLEM");
                }
            } catch (SQLException e) {
                request.getSession().setAttribute("error", "DATABASE_PROBLEM");
                logger.error("DBError", e);
            }
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("error", "ORDER_BY_ID_REQUEST_FAILED");
            logger.warn("WrongAccessTry", e);
        }
        try {
            requestDispatcher = request.getRequestDispatcher("/order" +".tiles");
            requestDispatcher.forward(request, response);
        } catch (ServletException e) {
            logger.error("Forward", e);
        } catch (IOException e) {
            logger.error("Forward", e);
        }
    }
}
