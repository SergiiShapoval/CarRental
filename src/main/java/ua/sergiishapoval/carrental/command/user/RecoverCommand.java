package ua.sergiishapoval.carrental.command.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.sergiishapoval.carrental.command.CommandTemplate;
import ua.sergiishapoval.carrental.dao.DaoFactory;
import ua.sergiishapoval.carrental.dao.DaoUser;
import ua.sergiishapoval.carrental.model.User;
import ua.sergiishapoval.carrental.model.UserErrors;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by Сергей on 27.12.2014.
 */
public class RecoverCommand extends CommandTemplate {

    private static final Logger logger = LoggerFactory.getLogger(RecoverCommand.class);
    
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        Locale locale = Locale.getDefault();

        ResourceBundle resourceBundle = null;
        try {
            resourceBundle = ResourceBundle.getBundle("language");
        } catch (MissingResourceException e) {
            resourceBundle = ResourceBundle.getBundle("language", Locale.ENGLISH);
            logger.info("ResourceBundleMissing", e);
        }

        User user = getUserFromParameters(request);

        request.getSession().removeAttribute("userError");
        UserErrors userErrors = new UserErrors();
        boolean isAnyError = false;
        
/*verifying form start*/
        if (!user.getEmail().matches("[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@" +
                "(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?")){
            isAnyError = true;
            userErrors.setEmail("WRONG_EMAIL");
        }
        if (user.getEmail().length() >= 55){
            isAnyError = true;
            userErrors.setEmail("BAD_LENGTH");
        }
/*verifying form end*/

/*receiving details on user from DB start*/
        if (!isAnyError) {
            DaoUser daoUser = null;
            try {
                daoUser = DaoFactory.getDaoUser();
            } catch (SQLException e) {
                logger.error("DBError", e);
            }
            try {
                if (!daoUser.findUserByEmail(user)){
                    isAnyError = true;
                    userErrors.setEmail("NO_USER_FOR_EMAIL");
                }
            } catch (SQLException e) {
                isAnyError = true;
                userErrors.setEmail("NO_USER_FOR_EMAIL");
            }
        }
/*receiving details on user from DB end*/
        if (isAnyError) {
            request.setAttribute("userError", userErrors);
            dispatcherForward(request, response, request.getRequestDispatcher("/recover.tiles"));
        } else {
            request.setAttribute("info", "RECOVER_EMAIL_SENT");
//receiving emailing properties begin
            Properties props = new Properties();
            String mailPropsPath = request.getSession().getServletContext().getRealPath("WEB-INF/classes/mail.properties");
            try {
                props.load(new FileReader(mailPropsPath));
            } catch (IOException e) {
                logger.error("MailPropsPath", e);
            }
            String userLogin = props.getProperty("userLogin");
            String userPassword = props.getProperty("userPassword");
//receiving emailing properties end
            
//            emailing session
            Session session = Session.getInstance(props,
                    new javax.mail.Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(userLogin, userPassword);
                        }
                    });
//preparing email content
            try {
                MimeMessage message = new MimeMessage(session);
                message.setHeader("Content-Type", "text/html; charset=UTF-8");
                message.setFrom(new InternetAddress(props.getProperty("userFrom")));
                try {
                    message.setSubject(MimeUtility.encodeText(resourceBundle.getString("RECOVER_EMAIL_TITLE"),  "UTF-8", "Q"));
                } catch (UnsupportedEncodingException e) {
                    logger.error("WrongEncoding", e);
                }
                
                String htmlBody = null;

//receiving prepared email template
                try {
                    BufferedReader reader = new BufferedReader(new FileReader(
                            request.getSession().getServletContext().getRealPath("WEB-INF/mail/recover.html")));
                    StringBuilder stringBuilder = new StringBuilder();
                    while (reader.ready()){
                        stringBuilder.append(reader.readLine());
                    }
                    htmlBody = stringBuilder.toString();
                } catch (IOException e) {
                    logger.error("MailFileReading", e);
                }
//changing variables in the letter
                htmlBody = htmlBody.replaceAll("\\$\\{firstname\\}", user.getFirstname());
                htmlBody = htmlBody.replaceAll("\\$\\{RECOVER_EMAIL_TITLE\\}", resourceBundle.getString("RECOVER_EMAIL_TITLE"));
                htmlBody = htmlBody.replaceAll("\\$\\{GREETING\\}", resourceBundle.getString("GREETING"));
                htmlBody = htmlBody.replaceAll("\\$\\{RECOVERY_EMAIL_MESSAGE\\}", resourceBundle.getString("RECOVERY_EMAIL_MESSAGE"));
                htmlBody = htmlBody.replaceAll("\\$\\{password\\}", user.getPassword());
                htmlBody = htmlBody.replaceAll("\\$\\{subject\\}", resourceBundle.getString("RECOVER_EMAIL_TITLE"));

                message.setContent(htmlBody, "text/html; charset=UTF-8");
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(user.getEmail()));
                message.setSentDate(new Date());
                Transport.send(message);
            } catch (MessagingException e) {
                logger.error("MailSendingError", e);
            }

            dispatcherForward(request, response, request.getRequestDispatcher("/info.tiles"));

        }
    }
}
