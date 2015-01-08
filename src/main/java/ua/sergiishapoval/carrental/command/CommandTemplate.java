package ua.sergiishapoval.carrental.command;


import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.sergiishapoval.carrental.model.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by Сергей on 22.12.2014.
 */
public class CommandTemplate implements Command {

    private static final Logger logger = LoggerFactory.getLogger(CommandTemplate.class);

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        infoRedirect(request, response, "BAD_COMMAND" );
    }
    
    public void infoRedirect(HttpServletRequest request, HttpServletResponse response, String message){
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/info.tiles");
        request.setAttribute("info", message);
        dispatcherForward(request, response, requestDispatcher);
    }

    public void dispatcherForward(HttpServletRequest request, HttpServletResponse response, RequestDispatcher requestDispatcher) {
        try {
            requestDispatcher.forward(request, response);
        } catch (ServletException e) {
            logger.error("Forward", e);
        } catch (IOException e) {
            logger.error("Forward", e);
        }
    }

    public boolean isAccessNotPermitted(HttpServletRequest request, HttpServletResponse response) {
        User user = (User) request.getSession().getAttribute("user");
        RequestDispatcher requestDispatcher = null;
        if (user == null || !user.getIsAdmin() ) {
            infoRedirect(request, response, "LOG_IN_WARN");
            return true;
        }
        return false;
    }


    public User getUserFromParameters(HttpServletRequest request) {
        User user = new User();
        try {
            BeanUtils.populate(user, request.getParameterMap());
        } catch (IllegalAccessException e) {
            logger.error("BeanUtilsError", e);
        } catch (InvocationTargetException e) {
            logger.error("BeanUtilsError", e);
        }
        return user;
    }
}
