package ua.sergiishapoval.carrental.model;

/**
 * Created by Сергей on 03.01.2015.
 */
public class Car {
    private Integer id;
    private String model;
    private String brand;
    private String className;
    private Double price;
    private Boolean isAutomat;
    private Boolean isDiesel;
    private Boolean hasCondition;
    private Integer doorQty;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Boolean getIsAutomat() {
        return isAutomat;
    }

    public void setIsAutomat(Boolean isAutomat) {
        this.isAutomat = isAutomat;
    }

    public Boolean getIsDiesel() {
        return isDiesel;
    }

    public void setIsDiesel(Boolean isDiesel) {
        this.isDiesel = isDiesel;
    }

    public Boolean getHasCondition() {
        return hasCondition;
    }

    public void setHasCondition(Boolean hasCondition) {
        this.hasCondition = hasCondition;
    }

    public Integer getDoorQty() {
        return doorQty;
    }

    public void setDoorQty(Integer doorQty) {
        this.doorQty = doorQty;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
