package com.youruniversity.marketplace.campus_marketplace;
import java.math.BigDecimal;
public class UpdateProductRequest{
    private String title;
    private String description;
    private BigDecimal price;
    private Category category;
    private Condition condition;
    private String location;
    private String contactPreference;
    private Boolean available;

    public String getTitle(){
        return title;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public String getDescription(){
        return description;
    }
    public void setDescription(String description){
        this.description = description;
    }
    public BigDecimal getPrice(){
        return price;
    }
    public void setPrice(BigDecimal price){
        this.price = price;
    }
    public Category getCategory(){
        return category;
    }
    public void setCategory(Category category){
        this.category = category;
    }
    public Condition getCondition(){
        return condition;
    }
    public void setCondition(Condition condition){
        this.condition = condition;
    }
    public String getLocation(){
        return location;
    }
    public void setLocation(String location){
        this.location = location;
    }
    public String getContactPreference(){
        return contactPreference;
    }
    public void setContactPreference(String contactPreference){
        this.contactPreference = contactPreference;
    }
    public Boolean getAvailable(){
        return available;
    }
    public void setAvailable(Boolean available){
        this.available = available;
    }
}