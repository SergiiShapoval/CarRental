package ua.sergiishapoval.carrental.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * Created by Сергей on 22.12.2014.
 */
public class DaoFactory {
    private static Logger logger = LoggerFactory.getLogger(DaoFactory.class);
    
    private static DataSource dataSource;
    static {
        try {
            InitialContext initialContext = new InitialContext();
            dataSource = (DataSource)  initialContext.lookup("java:comp/env/jdbc/carrental");
        } catch (NamingException e) {
            logger.error("DBCreateInit", e);
        }
    }

    public static DaoInitDestroy getDaoInitDestroy() throws SQLException {
        DaoInitDestroy daoInitDestroy = new DaoInitDestroy();
        daoInitDestroy.setConnection(dataSource.getConnection());
        return daoInitDestroy;
    }
    
    public static DaoCar getDaoCar() throws SQLException {
        DaoCar daoCar = new DaoCar();
        daoCar.setConnection(dataSource.getConnection());
        return daoCar;
    }


    public static DaoUser getDaoUser() throws SQLException {
        DaoUser daoUser = new DaoUser();
        daoUser.setConnection(dataSource.getConnection());
        return daoUser;
    }
    
    public static DaoOrder getDaoOrder() throws SQLException {
        DaoOrder daoOrder = new DaoOrder();
        daoOrder.setConnection(dataSource.getConnection());
        return daoOrder;
    }
}
