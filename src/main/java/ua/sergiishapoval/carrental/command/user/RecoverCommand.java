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
        ResourceBundle resourceBundle = getCorrectResourceBundle();
        User user = getUserFromParameters(request);
        request.getSession().removeAttribute("userError");
        UserErrors userErrors = new UserErrors();
        boolean isAnyError = verifyRecoverFormParams(user, userErrors);
        isAnyError = getPersistedUserDetails(user, userErrors, isAnyError);
        if (isAnyError) {
            request.setAttribute("userError", userErrors);
            dispatcherForward(request, response, request.getRequestDispatcher("/recover.tiles"));
        } else {
            request.setAttribute("info", "RECOVER_EMAIL_SENT");
            prepareAndSendEmail(request, resourceBundle, user);
            dispatcherForward(request, response, request.getRequestDispatcher("/info.tiles"));
        }
    }

    private void prepareAndSendEmail(HttpServletRequest request, ResourceBundle resourceBundle, User user) {
        Properties props = getEmailProperties(request);
        Session session = getEmailSession(props, props.getProperty("userLogin"), props.getProperty("userPassword"));
        try {
            MimeMessage message = setMimeMessage(resourceBundle, props, session);
            String htmlBody = getHtmlTemplate(request);
            htmlBody = presonalizeMessage(resourceBundle, user, htmlBody);
            setHtmlContent(user, message, htmlBody);
            Transport.send(message);
        } catch (UnsupportedEncodingException e) {
            logger.error("WrongEncoding", e);
        } catch (IOException e) {
            logger.error("MailFileReading", e);
        } catch (MessagingException e) {
            logger.error("MailSendingError", e);
        }
    }

    private void setHtmlContent(User user, MimeMessage message, String htmlBody) throws MessagingException {
        message.setContent(htmlBody, "text/html; charset=UTF-8");
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(user.getEmail()));
        message.setSentDate(new Date());
    }

    private String presonalizeMessage(ResourceBundle resourceBundle, User user, String htmlBody) {
        htmlBody = htmlBody.replaceAll("\\$\\{firstname\\}", user.getFirstname());
        htmlBody = htmlBody.replaceAll("\\$\\{RECOVER_EMAIL_TITLE\\}", resourceBundle.getString("RECOVER_EMAIL_TITLE"));
        htmlBody = htmlBody.replaceAll("\\$\\{GREETING\\}", resourceBundle.getString("GREETING"));
        htmlBody = htmlBody.replaceAll("\\$\\{RECOVERY_EMAIL_MESSAGE\\}", resourceBundle.getString("RECOVERY_EMAIL_MESSAGE"));
        htmlBody = htmlBody.replaceAll("\\$\\{password\\}", user.getPassword());
        htmlBody = htmlBody.replaceAll("\\$\\{subject\\}", resourceBundle.getString("RECOVER_EMAIL_TITLE"));
        return htmlBody;
    }

    private String getHtmlTemplate(HttpServletRequest request) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(
                request.getSession().getServletContext().getRealPath("WEB-INF/mail/recover.html")));
        StringBuilder stringBuilder = new StringBuilder();
        while (reader.ready()){
            stringBuilder.append(reader.readLine());
        }
        return stringBuilder.toString();
    }

    private MimeMessage setMimeMessage(ResourceBundle resourceBundle, Properties props, Session session) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = new MimeMessage(session);
        message.setHeader("Content-Type", "text/html; charset=UTF-8");
        message.setFrom(new InternetAddress(props.getProperty("userFrom")));
        message.setSubject(MimeUtility.encodeText(resourceBundle.getString("RECOVER_EMAIL_TITLE"), "UTF-8", "Q"));
        return message;
    }

    private Session getEmailSession(Properties props, final String userLogin, final String userPassword) {
        return Session.getInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(userLogin, userPassword);
                    }
                });
    }

    private Properties getEmailProperties(HttpServletRequest request) {
        Properties props = new Properties();
        String mailPropsPath = request.getSession().getServletContext().getRealPath("WEB-INF/classes/mail.properties");
        try {
            props.load(new FileReader(mailPropsPath));
        } catch (IOException e) {
            logger.error("MailPropsPath", e);
        }
        return props;
    }

    private boolean getPersistedUserDetails(User user, UserErrors userErrors, boolean isAnyError) {
        if (!isAnyError) {
            try {
                DaoUser daoUser = DaoFactory.getDaoUser();
                if (!daoUser.findByEmail(user)){
                    isAnyError = true;
                    userErrors.setEmail("NO_USER_FOR_EMAIL");
                }
            } catch (SQLException e) {
                isAnyError = true;
                userErrors.setEmail("NO_USER_FOR_EMAIL");
            }
        }
        return isAnyError;
    }

    private boolean verifyRecoverFormParams(User user, UserErrors userErrors) {
        boolean isAnyError = false;
        if (!user.getEmail().matches("[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@" +
                "(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?")){
            isAnyError = true;
            userErrors.setEmail("WRONG_EMAIL");
        }
        if (user.getEmail().length() >= 55){
            isAnyError = true;
            userErrors.setEmail("BAD_LENGTH");
        }
        return isAnyError;
    }

    private ResourceBundle getCorrectResourceBundle() {
        try {
            return ResourceBundle.getBundle("language");
        } catch (MissingResourceException e) {
            logger.info("ResourceBundleMissing", e);
            return ResourceBundle.getBundle("language", Locale.ENGLISH);
        }
    }
}
