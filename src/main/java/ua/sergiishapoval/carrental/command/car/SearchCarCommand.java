package ua.sergiishapoval.carrental.command.car;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.sergiishapoval.carrental.command.CommandTemplate;
import ua.sergiishapoval.carrental.dao.DaoCar;
import ua.sergiishapoval.carrental.dao.DaoFactory;
import ua.sergiishapoval.carrental.model.Car;
import ua.sergiishapoval.carrental.model.CarFilter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Сергей on 22.12.2014.
 */
public class SearchCarCommand extends CommandTemplate {

    private static final Logger logger = LoggerFactory.getLogger(SearchCarCommand.class);
    public static final int PAGE_LIMIT = 10;
    
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        /*getting data for pagination start*/
        String[] path = request.getServletPath().split("/");
        Integer pageNumber = null;
        if (path.length > 3){
            pageNumber = Integer.parseInt(path[3]);
        }else {
            pageNumber = 1;
        }
        /*getting data for pagination end*/
        
        
        /*Getting filter parameters start*/
        CarFilter carFilter = new CarFilter();
        if (request.getMethod().toLowerCase().equals("post")) {
            try {
                BeanUtils.populate(carFilter, request.getParameterMap());
            } catch (IllegalAccessException e) {
                logger.error("BeanUtilsError", e);
            } catch (InvocationTargetException e) {
                logger.error("BeanUtilsError", e);
            }
        } else {
            carFilter = (CarFilter) request.getSession().getAttribute("carFilter");
            if (carFilter == null) carFilter = new CarFilter();
        }
        /*Getting filter parameters end*/

        DaoCar daoCar = null;
        try {
            daoCar = DaoFactory.getDaoCar();
            
            List<Car> cars = daoCar.getFilteredCarsPage(pageNumber, PAGE_LIMIT, carFilter);
            Integer carCount = carFilter.getResultQty();

            int totalPages = carCount/PAGE_LIMIT + 1;
            int current = pageNumber;
            int begin = Math.max(1, current - 2);
            int end = Math.min(begin + 5, totalPages);

            request.getSession().setAttribute("beginIndex", begin);
            request.getSession().setAttribute("endIndex", end);
            request.getSession().setAttribute("currentIndex", pageNumber);
            request.getSession().setAttribute("totalPages", totalPages);
            request.getSession().setAttribute("cars", cars);
            request.getSession().setAttribute("carFilter", carFilter);

            RequestDispatcher requestDispatcher = request.getRequestDispatcher("/"+ path[1] +".tiles");
            try {
                requestDispatcher.forward(request, response);
            } catch (ServletException e) {
                logger.error("Forward", e);
            } catch (IOException e) {
                logger.error("Forward", e);
            }

        } catch (SQLException e) {
            infoRedirect(request, response, "DATABASE_PROBLEM" );
            logger.error("DBError", e);
        }
        

    }
}
