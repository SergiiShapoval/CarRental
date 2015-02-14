package ua.sergiishapoval.carrental.dao;


import ua.sergiishapoval.carrental.model.User;

import java.sql.*;
import java.util.ResourceBundle;

/**
 * Created by Сергей on 22.12.2014.
 */
public class DaoUser {
    Connection connection;

    final static ResourceBundle resourceBundle = ResourceBundle.getBundle("sqlstatements");

    public DaoUser() {
    }
    
    final static String INSERT_USER = resourceBundle.getString("INSERT_USER");
    final static String FIND_USER_BY_EMAIL = resourceBundle.getString("FIND_USER_BY_EMAIL");
    final static String FIND_USER_BY_ID = resourceBundle.getString("FIND_USER_BY_ID");
    final static  String FIND_USER_WHERE_EMAIL_AND_PASSWORD = resourceBundle.getString("FIND_USER_WHERE_EMAIL_AND_PASSWORD");

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void add(User user) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER);
        preparedStatement.setString(1, user.getFirstname());
        preparedStatement.setString(2, user.getLastname());
        preparedStatement.setString(3, user.getEmail());
        preparedStatement.setString(4, user.getPassport());
        preparedStatement.setString(5, user.getPassword());
        preparedStatement.setBoolean(6, user.getIsAdmin());
        preparedStatement.executeUpdate();
    }
    
    
    public boolean findByEmail(User user) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(FIND_USER_BY_EMAIL);
        preparedStatement.setString(1, user.getEmail());

        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next() ){
            setProps(user, resultSet);
            return true;
        }

        return false;
    }
    public User findById(int userId) throws SQLException {
        User user = new User();
        PreparedStatement preparedStatement = connection.prepareStatement(FIND_USER_BY_ID);
        preparedStatement.setInt(1, userId);

        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            setProps(user, resultSet);
        }

        return user;
    }

    public boolean findByEmailAndPassword(User user) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(FIND_USER_WHERE_EMAIL_AND_PASSWORD);
        preparedStatement.setString(1, user.getEmail());
        preparedStatement.setString(2, user.getPassword());

        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next() ){
            setProps(user, resultSet);
            return true;
        }
        return false;
    }

    private void setProps(User user, ResultSet resultSet) throws SQLException {
        user.setFirstname(resultSet.getString("firstname"));
        user.setLastname(resultSet.getString("lastname"));
        user.setPassport(resultSet.getString("passport"));
        user.setPassword(resultSet.getString("password"));
        user.setEmail(resultSet.getString("email"));
        user.setUserId(resultSet.getInt("user_id"));
        user.setIsAdmin(resultSet.getBoolean("is_admin"));
    }


}
