package ua.sergiishapoval.carrental.command.user;


import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.sergiishapoval.carrental.command.CommandTemplate;
import ua.sergiishapoval.carrental.dao.DaoFactory;
import ua.sergiishapoval.carrental.dao.DaoUser;
import ua.sergiishapoval.carrental.model.User;
import ua.sergiishapoval.carrental.model.UserErrors;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by Сергей on 22.12.2014.
 */
public class AuthCommand extends CommandTemplate {

    private static final Logger logger = LoggerFactory.getLogger(AuthCommand.class);
    
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {

        User user = getUserFromParameters(request);
/*clean previous attributes start*/
        request.getSession().removeAttribute("userError");
        request.getSession().removeAttribute("auth");
        UserErrors userErrors = new UserErrors();
        boolean isAnyError = false;
/*clean previous attributes start*/

/*handle path for apache tiles start, page doesn't changed during log in*/
        String[] path = request.getServletPath().split("/");
        RequestDispatcher requestDispatcher = null;
        if (path.length >= 2) {
            requestDispatcher = request.getRequestDispatcher("/" + path[1] + ".tiles");
        } else {
            requestDispatcher = request.getRequestDispatcher("/" + "index" + ".tiles");
        }
/*handle path for apache tiles start*/
/*verify user params start*/
        if (user.getEmail()!=null && user.getPassword()!= null &&
                !user.getEmail().equals("") && !user.getPassword().equals("")){
            DaoUser daoUser = null;
            try {
                daoUser = DaoFactory.getDaoUser();
            } catch (SQLException e) {
                logger.error("DBError", e);
            }

            try {
                isAnyError = !daoUser.findUserByEmailAndPassword(user);
                if (isAnyError) {
                    boolean isRegistered = daoUser.findUserByEmail(user);
                    if (isRegistered){
                        userErrors.setPassword("WRONG_PASSWORD");
                    } else {
                        userErrors.setEmail("NO_USER_FOR_EMAIL");
                    }
                }
            } catch (SQLException e) {
                userErrors.setEmail("BAD_DB_CONN");
                logger.error("DBError", e);
            }
        } else {
            isAnyError = true;
            userErrors.setEmail("BLANK_FIELDS");
        }
/*verify user params end*/

        request.getSession().setAttribute("user", user);

        if (isAnyError){
            request.getSession().setAttribute("userError", userErrors);
            request.getSession().setAttribute("auth", true);
        } else {
/*logic for cookies start*/
            String cookieOn = request.getParameter("cookieOn");
            if (cookieOn != null && cookieOn.equals("on")){

                Map<String, String> userMap = null;
                try {
                    userMap = BeanUtils.describe(user);
                } catch (IllegalAccessException e) {
                    logger.error("BeanUtilsError", e);
                } catch (InvocationTargetException e) {
                    logger.error("BeanUtilsError", e);
                } catch (NoSuchMethodException e) {
                    logger.error("BeanUtilsError", e);
                }
                for (String key:userMap.keySet()){
                    Cookie cookie = new Cookie(key, userMap.get(key));
                    cookie.setMaxAge(604800);
                    cookie.setPath("/");
                    cookie.setHttpOnly(true);
                    response.addCookie(cookie);
                }
            }
        }
/*logic for cookies end*/
        dispatcherForward(request, response, requestDispatcher);

    }
}
