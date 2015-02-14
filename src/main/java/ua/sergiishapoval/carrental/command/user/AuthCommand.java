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
        cleanSession(request);
        UserErrors userErrors = new UserErrors();
        RequestDispatcher requestDispatcher = getSamePageDispatcher(request);
        boolean isAnyError = verifyUserParams(request, user, userErrors);
        if (isAnyError){
            request.getSession().setAttribute("userError", userErrors);
            request.getSession().setAttribute("auth", true);
        } else {
            addAuthCookies(request, response, user);
        }
        dispatcherForward(request, response, requestDispatcher);

    }

    private void addAuthCookies(HttpServletRequest request, HttpServletResponse response, User user) {
        String cookieOn = request.getParameter("cookieOn");
        if (cookieOn != null && cookieOn.equals("on")){
            Map<String, String> userMap = null;
            try {
                userMap = BeanUtils.describe(user);
            } catch (Exception e) {
                logger.error("BeanUtilsError", e);
            }

            for (String key: userMap.keySet()){
                Cookie cookie = new Cookie(key, userMap.get(key));
                cookie.setMaxAge(604800);
                cookie.setPath("/");
                cookie.setHttpOnly(true);
                response.addCookie(cookie);
            }
        }
    }

    private boolean verifyUserParams(HttpServletRequest request, User user, UserErrors userErrors) {
        boolean isAnyError = false;
        if (isCredentialsWellFormed(user)){
            try {
                DaoUser daoUser = DaoFactory.getDaoUser();
                isAnyError = !daoUser.findByEmailAndPassword(user);
                if (isAnyError) {
                    boolean isRegistered = daoUser.findByEmail(user);
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
        request.getSession().setAttribute("user", user);
        return isAnyError;
    }

    private boolean isCredentialsWellFormed(User user) {
        return user.getEmail() != null && user.getPassword()!= null &&
                !user.getEmail().equals("") && !user.getPassword().equals("");
    }

    private void cleanSession(HttpServletRequest request) {
        request.getSession().removeAttribute("userError");
        request.getSession().removeAttribute("auth");
    }
}
