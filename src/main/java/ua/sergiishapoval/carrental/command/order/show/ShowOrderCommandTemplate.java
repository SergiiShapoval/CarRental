package ua.sergiishapoval.carrental.command.order.show;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.sergiishapoval.carrental.command.CommandTemplate;
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
public class ShowOrderCommandTemplate extends CommandTemplate {

    private static final Logger logger = LoggerFactory.getLogger(ShowOrderCommandTemplate.class);

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        if (isAccessNotPermitted(request, response)) return;
        RequestDispatcher requestDispatcher;
        try {
            int orderId = Integer.parseInt(request.getParameter("id"));
            Order orderChosen = null;

            DaoOrder daoOrder = null;
            try {
                daoOrder = DaoFactory.getDaoOrder();
                orderChosen = daoOrder.getOrderDataByOrderId(orderId);
/*handle request on not existing orders start*/
                if (orderChosen.getBrand() == null) {
                    infoRedirect(request, response, "ORDER_BY_ID_REQUEST_FAILED" );
/*handle request on not existing orders end*/
                } else {
                    request.getSession().setAttribute("order", orderChosen);
                    requestDispatcher = request.getRequestDispatcher("/order" + ".tiles");
                    try {
                        requestDispatcher.forward(request, response);
                    } catch (ServletException e) {
                        logger.error("Forward", e);
                    } catch (IOException e) {
                        logger.error("Forward", e);
                    }
                }
            } catch (SQLException e) {
                infoRedirect(request, response, "DATABASE_PROBLEM" );
                logger.error("DBError", e);
            }
        } catch (NumberFormatException e) {
            infoRedirect(request, response, "ORDER_BY_ID_REQUEST_FAILED" );
            logger.warn("WrongAccessTry", e);
        }
    }


}

