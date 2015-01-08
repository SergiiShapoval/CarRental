package ua.sergiishapoval.carrental.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Locale;

/**
 * Created by Сергей on 27.12.2014.
 */
public class LanguageCommand implements Command {
    private static final Logger logger = LoggerFactory.getLogger(LanguageCommand.class);
    
    
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        String language = null;
        language = request.getParameter("lang_id");
        if ((language) != null) {
            HttpSession httpSession = request.getSession();
            httpSession.setAttribute("lang_id", language);
            Locale locale = new Locale(language);
            Locale.setDefault(locale);
        }

/*path handling to show on the same page start*/
        String[] path = request.getServletPath().split("/");
        Integer pageNumber = null;
        RequestDispatcher requestDispatcher = null;
        if (path.length < 2)
            requestDispatcher = request.getRequestDispatcher("/index" +".tiles");
        else 
            requestDispatcher = request.getRequestDispatcher("/"+ path[1] +".tiles");
 /*path handling to show on the same page end*/
        try {
            requestDispatcher.forward(request, response);
        } catch (ServletException e) {
            logger.error("Forward", e);
        } catch (IOException e) {
            logger.error("Forward", e);
        }
    }
}
