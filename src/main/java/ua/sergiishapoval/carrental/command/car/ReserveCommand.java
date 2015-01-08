package ua.sergiishapoval.carrental.command.car;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.sergiishapoval.carrental.command.CommandTemplate;
import ua.sergiishapoval.carrental.dao.DaoCar;
import ua.sergiishapoval.carrental.dao.DaoFactory;
import ua.sergiishapoval.carrental.model.Car;
import ua.sergiishapoval.carrental.model.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Сергей on 27.12.2014.
 */
public class ReserveCommand extends CommandTemplate {

    private static final Logger logger = LoggerFactory.getLogger(ReserveCommand.class);
    
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        RequestDispatcher requestDispatcher = null;
/*user can't choose car in get parameters in security reasons, only through session attributes*/
        if (request.getMethod().toLowerCase().equals("get")){
            request.getSession().removeAttribute("error");
            int carId = Integer.parseInt(request.getParameter("id"));
            List<Car> cars = (List<Car>) request.getSession().getAttribute("cars");
            Car carChosen = null;
            for (Car car: cars){
                if (car.getId() == carId) {
                    carChosen = car;
                    break;
                }
            }
            request.getSession().setAttribute("car", carChosen);
            dispatcherForward(request, response, request.getRequestDispatcher("/reserve" +".tiles"));
        } else {
            Car car = (Car) request.getSession().getAttribute("car");
            User user = (User) request.getSession().getAttribute("user");
            String beginDateStr = request.getParameter("beginDate");
            String endDateStr = request.getParameter("endDate");

            boolean isAnyError = false;
            String error = null;
            
/*verifying dates start*/
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date beginDate = null;
            Date endDate = null;
            try {
                beginDate = (Date)dateFormat.parse(beginDateStr);
            } catch (ParseException e) {
                logger.error("DatesRecognition", e);
            }
            try {
                endDate = (Date)dateFormat.parse(endDateStr);
            } catch (ParseException e) {
                logger.error("DatesRecognition", e);
            }

            long timeDifference = endDate.getTime() - beginDate.getTime();
            int dayDifference = (int) (((timeDifference /1000) / 3600) / 24);
            if (dayDifference < 1){
                isAnyError = true;
                error = "INCORRECT_DATES";
/*verifying dates end*/
            } else {
                DaoCar daoCar = null;
                try {
                    daoCar = DaoFactory.getDaoCar();
                    boolean isAvailableCar = daoCar.checkCarAvailability(car.getId(), beginDateStr, endDateStr);
                    
                    if (!isAvailableCar) {
                        isAnyError = true;
                        error = "CAR_NOT_AVAILABLE";
                    } else {
/*car reservation start*/
                        daoCar.reserveCar(user.getUser_id(), car, beginDateStr, endDateStr, dayDifference );
/*car reservation end*/
                    }
                } catch (SQLException e) {
                    isAnyError = true;
                    error = "CAR_RESERVATION_FAIL_SQL";
                    logger.error("DBError", e);
                    
                }
            }
            if (isAnyError){
                request.getSession().setAttribute("error", error);
                dispatcherForward(request, response, request.getRequestDispatcher("/reserve" +".tiles"));
            } else {
                infoRedirect(request, response, "RESERVE_SUCCESS" );
            }
        }
    }
}
