package ua.sergiishapoval.carrental.command;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Сергей on 22.12.2014.
 */
public interface Command {
    void execute(HttpServletRequest httpRequest, HttpServletResponse httpResponse);
}
