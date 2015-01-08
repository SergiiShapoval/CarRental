package ua.sergiishapoval.carrental.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Сергей on 22.12.2014.
 */
public class IndexCommand extends CommandTemplate {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        String path = request.getServletPath();
        if (path.equals("/")) path = "/index";
        dispatcherForward(request, response, request.getRequestDispatcher(path +".tiles"));
    }
}
