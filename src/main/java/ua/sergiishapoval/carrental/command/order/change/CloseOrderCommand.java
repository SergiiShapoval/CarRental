package ua.sergiishapoval.carrental.command.order.change;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.sergiishapoval.carrental.command.Command;
import ua.sergiishapoval.carrental.dao.DaoFactory;
import ua.sergiishapoval.carrental.dao.DaoOrder;
import ua.sergiishapoval.carrental.model.Order;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;

/**
 * Created by Сергей on 27.12.2014.
 */
public class CloseOrderCommand implements Command {

    private static final Logger logger = LoggerFactory.getLogger(CloseOrderCommand.class);

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        try {
            request.getSession().removeAttribute("error");
            int orderId = Integer.parseInt(request.getParameter("id"));
            DaoOrder daoOrder = DaoFactory.getDaoOrder();
            boolean isUpdated = daoOrder.changeStatus(orderId, "closed");
            if (isUpdated) {
                Order orderChosen = daoOrder.getDataById(orderId);
                request.getSession().setAttribute("order", orderChosen);
            } else {
                request.getSession().setAttribute("error", "DATABASE_PROBLEM");
            }
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("/order" +".tiles");
            requestDispatcher.forward(request, response);

        } catch (NumberFormatException e) {
            request.getSession().setAttribute("error", "ORDER_BY_ID_REQUEST_FAILED");
            logger.warn("WrongAccessTry", e);
        } catch (SQLException e) {
            request.getSession().setAttribute("error", "DATABASE_PROBLEM");
            logger.error("DBError", e);
        } catch (Exception e) {
            logger.error("Forward", e);
        }
    }
}

