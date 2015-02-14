package ua.sergiishapoval.carrental.command.user;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.sergiishapoval.carrental.command.Command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Сергей on 22.12.2014.
 */
public class LogOutCommand implements Command {

    private static final Logger logger = LoggerFactory.getLogger(LogOutCommand.class);
    
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        request.getSession().invalidate();
        try {
            response.sendRedirect("/index");
        } catch (IOException e) {
            logger.error("Redirect", e);
        }
    }
}
