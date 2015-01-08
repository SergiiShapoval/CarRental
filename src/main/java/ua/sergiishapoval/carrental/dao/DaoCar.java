package ua.sergiishapoval.carrental.dao;


import ua.sergiishapoval.carrental.model.Car;
import ua.sergiishapoval.carrental.model.CarFilter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Сергей on 22.12.2014.
 */
public class DaoCar {
    Connection connection;

    final static ResourceBundle sqlResourceBundle = ResourceBundle.getBundle("sqlstatements");
    ResourceBundle langResourceBundle;

    public DaoCar() {
    }
    
    final static String CARS_FILTER_PAGE = sqlResourceBundle.getString("CARS_FILTER_PAGE") ;
    final static String REQUEST_LIMIT = sqlResourceBundle.getString("REQUEST_LIMIT");
    final static String DATE_ORDER_FILTER = sqlResourceBundle.getString("DATE_ORDER_FILTER");
    final static String BEGIN_FILTER_DATE_SET = sqlResourceBundle.getString("BEGIN_FILTER_DATE_SET");
    final static String END_FILTER_DATE_SET = sqlResourceBundle.getString("END_FILTER_DATE_SET");
    final static String CARS_FILTER_PAGE_COUNT = sqlResourceBundle.getString("CARS_FILTER_PAGE_COUNT");
    final static String CLASS_NAME = sqlResourceBundle.getString("CLASS_NAME");
    final static String HAS_CONDITION = sqlResourceBundle.getString("HAS_CONDITION");
    final static String IS_AUTOMAT = sqlResourceBundle.getString("IS_AUTOMAT");
    final static String IS_DIESEL = sqlResourceBundle.getString("IS_DIESEL");
    final static String PRICE = sqlResourceBundle.getString("PRICE");
    final static String CAR_ID = sqlResourceBundle.getString("CAR_ID");
    final static String CARS_CAR_ID = sqlResourceBundle.getString("CARS_CAR_ID");
    final static String MODEL_NAME = sqlResourceBundle.getString("MODEL_NAME");
    final static String BRAND_NAME = sqlResourceBundle.getString("BRAND_NAME");
    final static String DOOR_QTY = sqlResourceBundle.getString("DOOR_QTY");
    final static String RESERVE_REQUEST = sqlResourceBundle.getString("RESERVE_REQUEST");

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
    
    public void reserveCar(int userId, Car car, String beginDate, String endDate, int dayQty) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(RESERVE_REQUEST);
        preparedStatement.setInt(1, car.getId());
        preparedStatement.setInt(2, userId);
        preparedStatement.setString(3, beginDate);
        preparedStatement.setString(4, endDate);
        preparedStatement.setDouble(5, dayQty * car.getPrice());
        preparedStatement.executeUpdate();
    }


    public boolean checkCarAvailability(int carId, String beginDate, String endDate) throws SQLException {
/*if no car in request = car is available*/
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(DATE_ORDER_FILTER);
        stringBuilder.append(" " + CARS_CAR_ID + " =? ");
        stringBuilder.append(" AND (" + BEGIN_FILTER_DATE_SET ); 
        stringBuilder.append(" OR " + END_FILTER_DATE_SET + "))" );

        PreparedStatement preparedStatement = connection.prepareStatement(stringBuilder.toString());
        preparedStatement.setInt(1, carId);
        preparedStatement.setString(2, beginDate);
        preparedStatement.setString(3, beginDate);
        preparedStatement.setString(4, beginDate);
        preparedStatement.setString(5, endDate);
        
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()){
            return false;
        } else 
            return true;
    }
    
/*
    public Integer getCountCars() throws SQLException {
        Integer carCount = null;
        PreparedStatement preparedStatement = connection.prepareStatement(COUNT_CARS);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()){
            carCount = resultSet.getInt(1);
        }
        return carCount;
    }
*/

/*
    public List<Car> getCarsPage(int pageNumber, int pageLimit) throws SQLException {
        List<Car> cars = new ArrayList<>();
        PreparedStatement preparedStatement = connection.prepareStatement(CARS_PAGE);
        preparedStatement.setInt(1, (pageNumber - 1) * pageLimit);
        preparedStatement.setInt(2, pageLimit);

        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next() ){
            Car car = new Car();
            car.setId(resultSet.getInt(CAR_ID));
            car.setModel(resultSet.getString(MODEL_NAME));
            car.setBrand(resultSet.getString(BRAND_NAME));
            car.setClassName(resultSet.getString(CLASS_NAME));
            car.setPrice(resultSet.getDouble(PRICE));
            car.setIsAutomat(resultSet.getBoolean(IS_AUTOMAT));
            car.setIsDiesel(resultSet.getBoolean(IS_DIESEL));
            car.setHasCondition(resultSet.getBoolean(HAS_CONDITION));
            car.setDoorQty(resultSet.getInt(DOOR_QTY));
            cars.add(car);
        }
        
        return cars;
    }
*/

    public List<Car> getFilteredCarsPage(int pageNumber, int pageLimit, CarFilter carFilter) throws SQLException {
        List<Car> cars = new ArrayList<>();
        langResourceBundle = ResourceBundle.getBundle("language");
        String ANY = langResourceBundle.getString("ANY");
        String WITH_CONDITION = langResourceBundle.getString("WITH_CONDITION");
        String AUTOMAT = langResourceBundle.getString("AUTOMAT");
        String DIESEL = langResourceBundle.getString("DIESEL");


        StringBuilder stringBuilder = new StringBuilder();
        
        boolean isFirst = true;
        
/*adding filter criteria to request start*/
        if (!carFilter.getClassName().equals(ANY)){
            isFirst = setStatementStart(stringBuilder, isFirst);
            stringBuilder.append(" " + CLASS_NAME + " = '"+ carFilter.getClassName() + "'");
        }
        if (!carFilter.getHasCondition().equals(ANY)){
            isFirst = setStatementStart(stringBuilder, isFirst);
            if (carFilter.getHasCondition().equals(WITH_CONDITION)) {
                stringBuilder.append(" " + HAS_CONDITION + " = " + true);
            } else stringBuilder.append(" " + HAS_CONDITION + " = " + false);

        }
        if (!carFilter.getIsAutomat().equals(ANY)){
            isFirst = setStatementStart(stringBuilder, isFirst);
            if (carFilter.getIsAutomat().equals(AUTOMAT))
            stringBuilder.append(" " + IS_AUTOMAT + " = " + true);
            else stringBuilder.append(" " + IS_AUTOMAT + " = " + false);
        }
        if (!carFilter.getIsDiesel().equals(ANY)){
            isFirst = setStatementStart(stringBuilder, isFirst);
            if (carFilter.getIsDiesel().equals(DIESEL))
                stringBuilder.append(" " + IS_DIESEL + " = " + true);
            else stringBuilder.append(" " + IS_DIESEL + " = " + false);
        }
        if (!carFilter.getPrice().equals("")){
            isFirst = setStatementStart(stringBuilder, isFirst);
            stringBuilder.append(" " + PRICE +" <= "+ carFilter.getPrice());
        }
/*adding filter criteria to request end*/
        
/*date criteria adding start */
        /*dateCount = 2 => both set*/
        /*dateCount = 1 => begin set*/
        /*dateCount = -1 => end set*/
        /*dateCount = 0 => both null*/
        
        int dateCount =0;
        
        if (!carFilter.getBeginDate().equals("")){
            isFirst = setStatementStart(stringBuilder, isFirst);
            dateCount++;
            stringBuilder.append(" " + CAR_ID + " NOT IN (");
            stringBuilder.append(DATE_ORDER_FILTER);

            stringBuilder.append(BEGIN_FILTER_DATE_SET);
/*adding endDate to verify all cases, end date = Today + 100 years*/
            stringBuilder.append(" OR ");
            stringBuilder.append(END_FILTER_DATE_SET);
            stringBuilder.append(")");
        }
        if (!carFilter.getEndDate().equals("")){
            if (dateCount==0) {
                isFirst = setStatementStart(stringBuilder, isFirst);
                dateCount=-1;
                stringBuilder.append(" " + CAR_ID + " NOT IN (");
                stringBuilder.append(DATE_ORDER_FILTER);
                stringBuilder.append(END_FILTER_DATE_SET);
                stringBuilder.append(")");
            } else {
                dateCount++;
            }
            
        }
        
        if (dateCount!=0) stringBuilder.append(")");
/*date criteria adding end*/        
        
/*preparing two statements: with limit for result and result qty for pagination start*/  
        String resultRequest = CARS_FILTER_PAGE + stringBuilder.toString();
        PreparedStatement resultStatement = connection.prepareStatement(resultRequest +" "+ REQUEST_LIMIT);
        
        String qtyRequest = CARS_FILTER_PAGE_COUNT + stringBuilder.toString();
        PreparedStatement qtyStatement = connection.prepareStatement(qtyRequest);
        
/*preparing two statements: with limit for result and result qty for pagination end*/        
        switch (dateCount){
            case 2:
                resultStatement.setString(1, carFilter.getBeginDate());
                resultStatement.setString(2, carFilter.getBeginDate());
                resultStatement.setString(3, carFilter.getBeginDate());
                resultStatement.setString(4, carFilter.getEndDate());
                resultStatement.setInt(5, (pageNumber - 1) * pageLimit);
                resultStatement.setInt(6, pageLimit);

                qtyStatement.setString(1, carFilter.getBeginDate());
                qtyStatement.setString(2, carFilter.getBeginDate());
                qtyStatement.setString(3, carFilter.getBeginDate());
                qtyStatement.setString(4, carFilter.getEndDate());
                break;
            case 1:
/*adding endDate to verify all cases, end date = Today + 100 years*/
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date endDateSubstitute = new Date();
                endDateSubstitute.setYear(endDateSubstitute.getYear() + 100);
                String date = simpleDateFormat.format(endDateSubstitute);
                
                resultStatement.setString(1, carFilter.getBeginDate());
                resultStatement.setString(2, carFilter.getBeginDate());
                resultStatement.setString(3, carFilter.getBeginDate());
                resultStatement.setString(4, date);
                resultStatement.setInt(5, (pageNumber - 1) * pageLimit);
                resultStatement.setInt(6, pageLimit);

                qtyStatement.setString(1, carFilter.getBeginDate());
                qtyStatement.setString(2, carFilter.getBeginDate());
                qtyStatement.setString(3, carFilter.getBeginDate());
                qtyStatement.setString(4, date);
                break;
            case -1:
/*working with today, because begin date is not set*/
                SimpleDateFormat simpleDateFormatEnd = new SimpleDateFormat("yyyy-MM-dd");
                String dateStart = simpleDateFormatEnd.format(new Date());
                
                resultStatement.setString(1, dateStart);
                resultStatement.setString(2, carFilter.getEndDate());
                resultStatement.setInt(3, (pageNumber - 1) * pageLimit);
                resultStatement.setInt(4, pageLimit);

                qtyStatement.setString(1, dateStart);
                qtyStatement.setString(2, carFilter.getEndDate());
                break;
            default:
                resultStatement.setInt(1, (pageNumber - 1) * pageLimit);
                resultStatement.setInt(2, pageLimit);
        }

        ResultSet resultSet = resultStatement.executeQuery();
        while (resultSet.next() ){
            Car car = new Car();
            car.setId(resultSet.getInt(CAR_ID));
            car.setModel(resultSet.getString(MODEL_NAME));
            car.setBrand(resultSet.getString(BRAND_NAME));
            car.setClassName(resultSet.getString(CLASS_NAME));
            car.setPrice(resultSet.getDouble(PRICE));
            car.setIsAutomat(resultSet.getBoolean(IS_AUTOMAT));
            car.setIsDiesel(resultSet.getBoolean(IS_DIESEL));
            car.setHasCondition(resultSet.getBoolean(HAS_CONDITION));
            car.setDoorQty(resultSet.getInt(DOOR_QTY));
            cars.add(car);
        }

        ResultSet qtySet = qtyStatement.executeQuery();
        if (qtySet.next()){
            carFilter.setResultQty(qtySet.getInt(1));
        }

        
        return cars;
    }

    private boolean setStatementStart(StringBuilder stringBuilder, boolean isFirst) {
        if (isFirst) {
            isFirst = false;
            stringBuilder.append(" WHERE ");
        } else {
            stringBuilder.append(" AND ");
        }
        return isFirst;
    }
}
