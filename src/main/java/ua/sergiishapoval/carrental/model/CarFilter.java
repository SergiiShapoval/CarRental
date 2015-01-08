package ua.sergiishapoval.carrental.model;

import java.util.ResourceBundle;

/**
 * Created by Сергей on 03.01.2015.
 */
public class CarFilter {

    private String className;
    private String price;
    private String beginDate;
    private String endDate;
    private String isAutomat;
    private String isDiesel;
    private String hasCondition;
    private int resultQty;

    public CarFilter() {
        ResourceBundle langResourceBundle = ResourceBundle.getBundle("language");
        className = langResourceBundle.getString("ANY");
        price = "";
        beginDate ="";
        endDate = "";
        isAutomat = langResourceBundle.getString("ANY");
        isDiesel = langResourceBundle.getString("ANY");
        hasCondition = langResourceBundle.getString("ANY");
    }

    public int getResultQty() {
        return resultQty;
    }

    public void setResultQty(int resultQty) {
        this.resultQty = resultQty;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getIsAutomat() {
        return isAutomat;
    }

    public void setIsAutomat(String isAutomat) {
        this.isAutomat = isAutomat;
    }

    public String getIsDiesel() {
        return isDiesel;
    }

    public void setIsDiesel(String isDiesel) {
        this.isDiesel = isDiesel;
    }

    public String getHasCondition() {
        return hasCondition;
    }

    public void setHasCondition(String hasCondition) {
        this.hasCondition = hasCondition;
    }
}
