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

    private enum DateFilterStatus {
        BEGIN_END_SET,
        BEGIN_SET,
        END_SET,
        BOTH_NULL
    }

    public DaoCar() {
    }

    final static String CARS_FILTER_PAGE = sqlResourceBundle.getString("CARS_FILTER_PAGE") ;
    final static String REQUEST_LIMIT = sqlResourceBundle.getString("REQUEST_LIMIT");
    final static String DATE_ORDER_FILTER = sqlResourceBundle.getString("DATE_ORDER_FILTER");
    final static String FILTER_DATE_CONDITION1 = sqlResourceBundle.getString("FILTER_DATE_CONDITION1");
    final static String FILTER_DATE_CONDITION2 = sqlResourceBundle.getString("FILTER_DATE_CONDITION2");
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

    public void reserve(int userId, Car car, String beginDate, String endDate, int dayQty) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(RESERVE_REQUEST);
        preparedStatement.setInt(1, car.getId());
        preparedStatement.setInt(2, userId);
        preparedStatement.setString(3, beginDate);
        preparedStatement.setString(4, endDate);
        preparedStatement.setDouble(5, dayQty * car.getPrice());
        preparedStatement.executeUpdate();
    }


    public boolean isAvailable(int carId, String beginDate, String endDate) throws SQLException {
/*if no car in request = car is available*/
        StringBuilder preparedStatementBuilder = new StringBuilder();
        preparedStatementBuilder.append(DATE_ORDER_FILTER);
        preparedStatementBuilder.append(" " + CARS_CAR_ID + " =? ");
        preparedStatementBuilder.append(" AND (" + FILTER_DATE_CONDITION1);
        preparedStatementBuilder.append(" OR " + FILTER_DATE_CONDITION2 + "))" );

        PreparedStatement preparedStatement = connection.prepareStatement(preparedStatementBuilder.toString());
        preparedStatement.setInt(1, carId);
        preparedStatement.setString(2, beginDate);
        preparedStatement.setString(3, beginDate);
        preparedStatement.setString(4, beginDate);
        preparedStatement.setString(5, endDate);
        ResultSet resultSet = preparedStatement.executeQuery();

        return !resultSet.next();
    }

    public List<Car> getFilteredPage(int pageNumber, int pageLimit, CarFilter carFilter) throws SQLException {
        List<Car> cars = new ArrayList<>();
        StringBuilder preparedStatementBuilder = new StringBuilder();
        DateFilterStatus dateFilterStatus = createFilterCriteria(carFilter, preparedStatementBuilder);
/*preparing two statements: with limit for result and result qty for pagination start*/
        String resultRequest = CARS_FILTER_PAGE + preparedStatementBuilder.toString();
        PreparedStatement resultStatement = connection.prepareStatement(resultRequest +" "+ REQUEST_LIMIT);

        String qtyRequest = CARS_FILTER_PAGE_COUNT + preparedStatementBuilder.toString();
        PreparedStatement qtyStatement = connection.prepareStatement(qtyRequest);
/*preparing two statements: with limit for result and result qty for pagination end*/

        /*adding endDate to verify all cases, end date = Today + 100 years*/
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String endDateSubstituteString = createEndDateSubstitute(simpleDateFormat);
        String startDateSubstituteString = simpleDateFormat.format(new Date());

        setQtyStatement(carFilter, dateFilterStatus, qtyStatement, endDateSubstituteString, startDateSubstituteString);
        setResultStatement(pageNumber, pageLimit, carFilter, dateFilterStatus, resultStatement, endDateSubstituteString, startDateSubstituteString);

        ResultSet resultSet = resultStatement.executeQuery();
        getCarsFromResultSet(cars, resultSet);

        ResultSet qtySet = qtyStatement.executeQuery();
        if (qtySet.next()){
            carFilter.setResultQty(qtySet.getInt(1));
        }
        return cars;
    }

    private String createEndDateSubstitute(SimpleDateFormat simpleDateFormat) {
        Date endDateSubstitute = new Date();
        endDateSubstitute.setYear(endDateSubstitute.getYear() + 100);
        return simpleDateFormat.format(endDateSubstitute);
    }

    private void getCarsFromResultSet(List<Car> cars, ResultSet resultSet) throws SQLException {
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
    }

    private void setResultStatement(int pageNumber, int pageLimit, CarFilter carFilter, DateFilterStatus dateFilterStatus, PreparedStatement resultStatement, String endDateSubstituteString, String startDateSubstituteString) throws SQLException {
        switch (dateFilterStatus){
            case BEGIN_END_SET:
                resultStatement.setString(1, carFilter.getBeginDate());
                resultStatement.setString(2, carFilter.getBeginDate());
                resultStatement.setString(3, carFilter.getBeginDate());
                resultStatement.setString(4, carFilter.getEndDate());
                resultStatement.setInt(5, (pageNumber - 1) * pageLimit);
                resultStatement.setInt(6, pageLimit);
                break;
            case BEGIN_SET:
                resultStatement.setString(1, carFilter.getBeginDate());
                resultStatement.setString(2, carFilter.getBeginDate());
                resultStatement.setString(3, carFilter.getBeginDate());
                /*endDateSubstituteString needed to verify all cases*/
                resultStatement.setString(4, endDateSubstituteString);
                resultStatement.setInt(5, (pageNumber - 1) * pageLimit);
                resultStatement.setInt(6, pageLimit);
                break;
            case END_SET:
                resultStatement.setString(1, startDateSubstituteString);
                resultStatement.setString(2, carFilter.getEndDate());
                resultStatement.setInt(3, (pageNumber - 1) * pageLimit);
                resultStatement.setInt(4, pageLimit);
                break;
            default:
                resultStatement.setInt(1, (pageNumber - 1) * pageLimit);
                resultStatement.setInt(2, pageLimit);
        }
    }

    private void setQtyStatement(CarFilter carFilter, DateFilterStatus dateFilterStatus, PreparedStatement qtyStatement, String endDateSubstituteString, String startDateSubstituteString) throws SQLException {
        switch (dateFilterStatus){
            case BEGIN_END_SET:
                qtyStatement.setString(1, carFilter.getBeginDate());
                qtyStatement.setString(2, carFilter.getBeginDate());
                qtyStatement.setString(3, carFilter.getBeginDate());
                qtyStatement.setString(4, carFilter.getEndDate());
                break;
            case BEGIN_SET:
                qtyStatement.setString(1, carFilter.getBeginDate());
                qtyStatement.setString(2, carFilter.getBeginDate());
                qtyStatement.setString(3, carFilter.getBeginDate());
                qtyStatement.setString(4, endDateSubstituteString);
                break;
            case END_SET:
                qtyStatement.setString(1, startDateSubstituteString);
                qtyStatement.setString(2, carFilter.getEndDate());
                break;
        }
    }

    private DateFilterStatus createFilterCriteria(CarFilter carFilter, StringBuilder preparedStatementBuilder) {
        boolean isFirstStatementAdded = createFilterExceptDate(carFilter, preparedStatementBuilder);
        return createDateFilter(carFilter, preparedStatementBuilder, isFirstStatementAdded);
    }

    private DateFilterStatus createDateFilter(CarFilter carFilter, StringBuilder preparedStatementBuilder, boolean isFirstStatementAdded) {
        DateFilterStatus dateFilterStatus = DateFilterStatus.BOTH_NULL;
        if (!carFilter.getBeginDate().equals("")){
            isFirstStatementAdded = setStatementStart(preparedStatementBuilder, isFirstStatementAdded);
            dateFilterStatus = DateFilterStatus.BEGIN_SET;
            preparedStatementBuilder.append(" " + CAR_ID + " NOT IN (");
            preparedStatementBuilder.append(DATE_ORDER_FILTER);
            preparedStatementBuilder.append(FILTER_DATE_CONDITION1);
            preparedStatementBuilder.append(" OR ");
            preparedStatementBuilder.append(FILTER_DATE_CONDITION2);
            preparedStatementBuilder.append(")");
        }
        if (!carFilter.getEndDate().equals("")){
            if (dateFilterStatus == DateFilterStatus.BOTH_NULL) {
                isFirstStatementAdded = setStatementStart(preparedStatementBuilder, isFirstStatementAdded);
                dateFilterStatus = DateFilterStatus.END_SET;
                preparedStatementBuilder.append(" " + CAR_ID + " NOT IN (");
                preparedStatementBuilder.append(DATE_ORDER_FILTER);
                preparedStatementBuilder.append(FILTER_DATE_CONDITION2);
                preparedStatementBuilder.append(")");
            } else {
                dateFilterStatus = DateFilterStatus.BEGIN_END_SET;
            }
        }
        if (dateFilterStatus != DateFilterStatus.BOTH_NULL) preparedStatementBuilder.append(")");
        return dateFilterStatus;
    }

    private boolean createFilterExceptDate(CarFilter carFilter, StringBuilder preparedStatementBuilder) {
        /*filter depends on language settings*/
        langResourceBundle = ResourceBundle.getBundle("language");
        String ANY = langResourceBundle.getString("ANY");
        String WITH_CONDITION = langResourceBundle.getString("WITH_CONDITION");
        String AUTOMAT = langResourceBundle.getString("AUTOMAT");
        String DIESEL = langResourceBundle.getString("DIESEL");

        boolean isFirstStatementAdded = false;
        isFirstStatementAdded = addClassFilter(carFilter.getClassName(), preparedStatementBuilder, ANY, isFirstStatementAdded);
        isFirstStatementAdded = addConditionFilter(carFilter.getHasCondition(), preparedStatementBuilder, ANY, WITH_CONDITION, isFirstStatementAdded);
        isFirstStatementAdded = addTransmissionFilter(carFilter.getIsAutomat(), preparedStatementBuilder, ANY, AUTOMAT, isFirstStatementAdded);
        isFirstStatementAdded = addFuelFilter(carFilter.getIsDiesel(), preparedStatementBuilder, ANY, DIESEL, isFirstStatementAdded);
        isFirstStatementAdded = addPriceFilter(carFilter.getPrice(), preparedStatementBuilder, isFirstStatementAdded);

        return isFirstStatementAdded;
    }

    private boolean addPriceFilter(String priceString, StringBuilder preparedStatementBuilder, boolean isFirstStatementAdded) {
        if (!priceString.equals("")){
            isFirstStatementAdded = setStatementStart(preparedStatementBuilder, isFirstStatementAdded);
            preparedStatementBuilder.append(" " + PRICE +" <= "+ priceString);
        }
        return isFirstStatementAdded;
    }

    private boolean addFuelFilter(String carFuel, StringBuilder preparedStatementBuilder, String ANY, String DIESEL, boolean isFirstStatementAdded) {
        if (!carFuel.equals(ANY)){
            isFirstStatementAdded = setStatementStart(preparedStatementBuilder, isFirstStatementAdded);
            preparedStatementBuilder.append(" " + IS_DIESEL + " = " + carFuel.equals(DIESEL));
        }
        return isFirstStatementAdded;
    }

    private boolean addTransmissionFilter(String carTransmission, StringBuilder preparedStatementBuilder, String ANY, String AUTOMAT, boolean isFirstStatementAdded) {
        if (!carTransmission.equals(ANY)){
            isFirstStatementAdded = setStatementStart(preparedStatementBuilder, isFirstStatementAdded);
            preparedStatementBuilder.append(" " + IS_AUTOMAT + " = " + carTransmission.equals(AUTOMAT));
        }
        return isFirstStatementAdded;
    }

    private boolean addConditionFilter(String carCondition, StringBuilder preparedStatementBuilder, String ANY, String WITH_CONDITION, boolean isFirstStatementAdded) {
        if (!carCondition.equals(ANY)){
            isFirstStatementAdded = setStatementStart(preparedStatementBuilder, isFirstStatementAdded);
            preparedStatementBuilder.append(" " + HAS_CONDITION + " = " + carCondition.equals(WITH_CONDITION));
        }
        return isFirstStatementAdded;
    }

    private boolean addClassFilter(String carClass, StringBuilder preparedStatementBuilder, String ANY, boolean isFirstStatementAdded) {
        if (!carClass.equals(ANY)){
            isFirstStatementAdded = setStatementStart(preparedStatementBuilder, isFirstStatementAdded);
            preparedStatementBuilder.append(" " + CLASS_NAME + " = '"+ carClass + "'");
        }
        return isFirstStatementAdded;
    }

    private boolean setStatementStart(StringBuilder preparedStatementBuilder, boolean isFirstStatementAdded) {
        if (!isFirstStatementAdded) {
            isFirstStatementAdded = true;
            preparedStatementBuilder.append(" WHERE ");
        } else {
            preparedStatementBuilder.append(" AND ");
        }
        return isFirstStatementAdded;
    }
}
