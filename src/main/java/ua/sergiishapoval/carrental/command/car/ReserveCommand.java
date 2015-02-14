package ua.sergiishapoval.carrental.command.car;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.sergiishapoval.carrental.command.CommandTemplate;
import ua.sergiishapoval.carrental.dao.DaoCar;
import ua.sergiishapoval.carrental.dao.DaoFactory;
import ua.sergiishapoval.carrental.model.Car;
import ua.sergiishapoval.carrental.model.User;

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
        if (request.getMethod().toLowerCase().equals("get")){
            /*car can be requested only through post*/
            redirectToReservePageWithCarSetted(request, response);
            return;
        }

        Car car = (Car) request.getSession().getAttribute("car");
        User user = (User) request.getSession().getAttribute("user");
        String beginDateStr = request.getParameter("beginDate");
        String endDateStr = request.getParameter("endDate");
        int dayDifference = getDiffInDays(beginDateStr, endDateStr);
        if (dayDifference < 1){
            request.getSession().setAttribute("error", "INCORRECT_DATES");
            dispatcherForward(request, response, request.getRequestDispatcher("/reserve" +".tiles"));
            return;
        }

        try {
            DaoCar daoCar = DaoFactory.getDaoCar();
            if (!daoCar.isAvailable(car.getId(), beginDateStr, endDateStr)) {
                request.getSession().setAttribute("error", "CAR_NOT_AVAILABLE");
                dispatcherForward(request, response, request.getRequestDispatcher("/reserve" +".tiles"));
            } else {
                daoCar.reserve(user.getUserId(), car, beginDateStr, endDateStr, dayDifference);
                infoRedirect(request, response, "RESERVE_SUCCESS" );
            }
        } catch (SQLException e) {
            request.getSession().setAttribute("error", "CAR_RESERVATION_FAIL_SQL");
            dispatcherForward(request, response, request.getRequestDispatcher("/reserve" +".tiles"));
            logger.error("DBError", e);
        }
    }

    private void redirectToReservePageWithCarSetted(HttpServletRequest request, HttpServletResponse response) {
        request.getSession().removeAttribute("error");
        int carId = Integer.parseInt(request.getParameter("id"));
        Car carChosen = findChosenCar(request, carId);
        request.getSession().setAttribute("car", carChosen);
        dispatcherForward(request, response, request.getRequestDispatcher("/reserve" +".tiles"));
    }

    private Car findChosenCar(HttpServletRequest request, int carId) {
        List<Car> cars = (List<Car>) request.getSession().getAttribute("cars");
        Car carChosen = null;
        for (Car car: cars){
            if (car.getId() == carId) {
                carChosen = car;
                break;
            }
        }
        return carChosen;
    }

    private int getDiffInDays(String beginDateStr, String endDateStr) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date beginDate = null;
        Date endDate = null;
        try {
            beginDate = dateFormat.parse(beginDateStr);
            endDate = dateFormat.parse(endDateStr);
        } catch (ParseException e) {
            logger.error("DatesRecognition", e);
        }
        long timeDifference = endDate.getTime() - beginDate.getTime();
        return (int) (((timeDifference /1000) / 3600) / 24);
    }
}
