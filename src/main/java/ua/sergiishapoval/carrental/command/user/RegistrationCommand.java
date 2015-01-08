package ua.sergiishapoval.carrental.command.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.sergiishapoval.carrental.command.CommandTemplate;
import ua.sergiishapoval.carrental.dao.DaoFactory;
import ua.sergiishapoval.carrental.dao.DaoUser;
import ua.sergiishapoval.carrental.model.User;
import ua.sergiishapoval.carrental.model.UserErrors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;

/**
 * Created by Сергей on 27.12.2014.
 */
public class RegistrationCommand extends CommandTemplate {

    private static final Logger logger = LoggerFactory.getLogger(RegistrationCommand.class);
    
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        User user = getUserFromParameters(request);

        request.getSession().removeAttribute("userError");
/*validation start*/        
        UserErrors userErrors = new UserErrors();
        boolean isAnyError = false;
        if (!user.getPassword().equals(user.getPasswordCheck()) || user.getPassword().equals("")){
            isAnyError = true;
            userErrors.setPassword("INCORRECT_PASSWORD");
        }else {
            if (!user.getFirstname().matches("[A-Za-zА-Яа-я]+")){
                isAnyError = true;
                userErrors.setFirstname("BLANK_WRONG_SYMBOLS");
            } 
            if (user.getFirstname().length() >= 55){
                isAnyError = true;
                userErrors.setFirstname("BAD_LENGTH");
            }
            if (!user.getLastname().matches("[A-Za-zА-Яа-я]+")){
                isAnyError = true;
                userErrors.setLastname("BLANK_WRONG_SYMBOLS");
            }
            if (user.getLastname().length() >= 55){
                isAnyError = true;
                userErrors.setLastname("BAD_LENGTH");
            }
            if (!user.getPassport().matches("[A-Za-zА-Яа-я0-9 ]+")){
                isAnyError = true;
                userErrors.setPassport("BLANK_WRONG_SYMBOLS");
            }
            if (user.getPassport().length() >= 55){
                isAnyError = true;
                userErrors.setPassport("BAD_LENGTH");
            }
            if (!user.getEmail().matches("[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@" +
                    "(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?")){
                isAnyError = true;
                userErrors.setEmail("WRONG_EMAIL");
            }
            if (user.getEmail().length() >= 55){
                isAnyError = true;
                userErrors.setEmail("BAD_LENGTH");
            }
        }
/*validation end*/
        
/*user adding start*/        
        if (!isAnyError) {
            DaoUser daoUser = null;
            try {
                daoUser = DaoFactory.getDaoUser();
            } catch (SQLException e) {
                logger.error("DBError", e);
            }

            try {
                daoUser.insertUser(user);
            } catch (SQLException e) {
                isAnyError = true;
                userErrors.setEmail("DOUBLE_EMAIL");
                logger.warn("DBError", e);
            }
        }
/*user adding end*/
        
        request.getSession().setAttribute("user", user);
        
        if (isAnyError) {
            request.getSession().setAttribute("userError", userErrors);
            dispatcherForward(request, response, request.getRequestDispatcher("/registration.tiles"));
        } else {
            infoRedirect(request, response, "REG_SUCCESS" );
        }
    }

}
