package ua.sergiishapoval.carrental.dao;


import ua.sergiishapoval.carrental.model.Order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Сергей on 22.12.2014.
 */
public class DaoOrder {
    Connection connection;

    final static ResourceBundle sqlResourceBundle = ResourceBundle.getBundle("sqlstatements");

    public DaoOrder() {
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    final static String MY_ORDERS_REQUESTS_SELECT = sqlResourceBundle.getString("MY_ORDERS_REQUESTS_SELECT");
    final static String ALL_ORDERS_REQUESTS_SELECT = sqlResourceBundle.getString("ALL_ORDERS_REQUESTS_SELECT");
    final static String ORDERS_REQUESTS_SELECT_BY_ID = sqlResourceBundle.getString("ORDERS_REQUESTS_SELECT_BY_ID");
    final static String UPDATE_ORDER_STATUS = sqlResourceBundle.getString("UPDATE_ORDER_STATUS");
    final static String UPDATE_ORDER_REASON = sqlResourceBundle.getString("UPDATE_ORDER_REASON");
    final static String UPDATE_ORDER_PENALTY = sqlResourceBundle.getString("UPDATE_ORDER_PENALTY");


    public boolean changePenalty(int orderId, double penalty) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_ORDER_PENALTY);

        preparedStatement.setDouble(1, penalty);
        preparedStatement.setInt(2, orderId);
        return preparedStatement.executeUpdate() == 1;
    }

    public boolean changeReason(int orderId, String reason) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_ORDER_REASON);

        preparedStatement.setString(1, reason);
        preparedStatement.setInt(2, orderId);
        return preparedStatement.executeUpdate() ==1;
    }


    public boolean changeStatus(int orderId, String status) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_ORDER_STATUS);

        preparedStatement.setString(1, status);
        preparedStatement.setInt(2, orderId);
        return preparedStatement.executeUpdate() ==1;
    }

    public Order getDataById(int orderId) throws SQLException {
        Order order = new Order();

        PreparedStatement preparedStatement = connection.prepareStatement(ORDERS_REQUESTS_SELECT_BY_ID);
        preparedStatement.setInt(1, orderId);

        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next() ){
            order = setProps(resultSet);
        }

        return order;
    }

    public List<Order> getByUserIdAndStatus(int userId, String status) throws SQLException {
        List<Order> orders = new ArrayList<>();

        PreparedStatement preparedStatement = connection.prepareStatement(MY_ORDERS_REQUESTS_SELECT);
        preparedStatement.setInt(1, userId);
        preparedStatement.setString(2, status);

        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next() ){
            Order order = setProps(resultSet);
            orders.add(order);
        }
        
        return orders;
    }

    public List<Order> getAllData(String status) throws SQLException {
        List<Order> orders = new ArrayList<>();

        PreparedStatement preparedStatement = connection.prepareStatement(ALL_ORDERS_REQUESTS_SELECT);
        preparedStatement.setString(1, status);

        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next() ){
            Order order = setProps(resultSet);
            orders.add(order);
        }
        
        return orders;
    }


    private Order setProps(ResultSet resultSet) throws SQLException {
        Order order = new Order();

        order.setBrand(resultSet.getString("brand_name"));
        order.setModel(resultSet.getString("model_name"));
        order.setRentTotal(resultSet.getDouble("rent_total"));
        order.setPenalty(resultSet.getDouble("penalty"));
        order.setReason(resultSet.getString("reason"));
        order.setStatus(resultSet.getString("status"));
        order.setDateStart(resultSet.getDate("date_start"));
        order.setDateEnd(resultSet.getDate("date_end"));
        order.setCarId(resultSet.getInt("car_id"));
        order.setOrderId(resultSet.getInt("order_id"));
        order.setUserId(resultSet.getInt("user_id"));
        return order;
    }

}
