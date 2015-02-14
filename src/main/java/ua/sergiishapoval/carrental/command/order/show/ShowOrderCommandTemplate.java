package ua.sergiishapoval.carrental.command.order.show;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.sergiishapoval.carrental.command.CommandTemplate;
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
public class ShowOrderCommandTemplate extends CommandTemplate {

    private static final Logger logger = LoggerFactory.getLogger(ShowOrderCommandTemplate.class);

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        if (isAccessNotPermitted(request, response)) return;
        try {
            int orderId = Integer.parseInt(request.getParameter("id"));
            DaoOrder daoOrder = DaoFactory.getDaoOrder();
            Order orderChosen = daoOrder.getDataById(orderId);
            if (orderChosen.getBrand() == null) {
                infoRedirect(request, response, "ORDER_BY_ID_REQUEST_FAILED" );
            } else {
                request.getSession().setAttribute("order", orderChosen);
                RequestDispatcher requestDispatcher = request.getRequestDispatcher("/order" + ".tiles");
                requestDispatcher.forward(request, response);
            }
        } catch (NumberFormatException e) {
            infoRedirect(request, response, "ORDER_BY_ID_REQUEST_FAILED" );
            logger.warn("WrongAccessTry", e);
        } catch (SQLException e) {
            infoRedirect(request, response, "DATABASE_PROBLEM" );
            logger.error("DBError", e);
        } catch (Exception e) {
            logger.error("Forward", e);
        }
    }


}

