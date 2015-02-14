package ua.sergiishapoval.carrental.command.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.sergiishapoval.carrental.command.CommandTemplate;
import ua.sergiishapoval.carrental.dao.DaoFactory;
import ua.sergiishapoval.carrental.dao.DaoUser;
import ua.sergiishapoval.carrental.model.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;

/**
 * Created by Сергей on 27.12.2014.
 */
public class UserInfoCommand extends CommandTemplate {

    private static final Logger logger = LoggerFactory.getLogger(UserInfoCommand.class);

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        if (isAccessNotPermitted(request, response)) return;
        try {
            int userId = Integer.parseInt(request.getParameter("id"));
            DaoUser daoUser = DaoFactory.getDaoUser();
            User userChosen = daoUser.findById(userId);
/*handle request on not existing users start*/
            if (userChosen.getEmail() == null) {
                infoRedirect(request, response, "USER_BY_ID_REQUEST_FAILED" );
/*handle request on not existing users end*/
            } else {
                request.getSession().setAttribute("userInfo", userChosen);
                RequestDispatcher requestDispatcher = request.getRequestDispatcher("/user" + ".tiles");
                requestDispatcher.forward(request, response);
            }
        } catch (NumberFormatException e) {
            infoRedirect(request, response, "USER_BY_ID_REQUEST_FAILED" );
            logger.warn("User By ID request Failed", e);
        } catch (SQLException e) {
            infoRedirect(request, response, "DATABASE_PROBLEM" );
            logger.error("Forward", e);
        } catch (Exception e) {
            logger.error("Forward", e);
        }
    }
}

