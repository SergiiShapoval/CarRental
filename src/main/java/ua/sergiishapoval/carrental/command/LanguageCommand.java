package ua.sergiishapoval.carrental.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Locale;

/**
 * Created by Сергей on 27.12.2014.
 */
public class LanguageCommand extends CommandTemplate {
    private static final Logger logger = LoggerFactory.getLogger(LanguageCommand.class);
    
    
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        String language = request.getParameter("lang_id");
        if ((language) != null) {
            HttpSession httpSession = request.getSession();
            httpSession.setAttribute("lang_id", language);
            Locale locale = new Locale(language);
            Locale.setDefault(locale);
        }

        dispatchOnSamePage(request, response);
    }

    private void dispatchOnSamePage(HttpServletRequest request, HttpServletResponse response) {
        RequestDispatcher requestDispatcher = getSamePageDispatcher(request);

        try {
            requestDispatcher.forward(request, response);
        } catch (Exception e) {
            logger.error("Forward", e);
        }
    }


}
