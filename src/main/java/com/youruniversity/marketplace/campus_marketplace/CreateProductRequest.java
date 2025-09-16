package com.youruniversity.marketplace.campus_marketplace;
import jakarta.validation.constratins.*;
import java.math.BigDecimal;

public class CreateProductRequest{
    @NotBlank(message = "Title is required")
    private String title;
    @NotBlank(message = "Description is required")
    private String description;
    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", message = "Price must be greater than or equal to 0.0")
    private BigDecimal price;
    @NotNull(message = "Category is required")
    private Category category;
    @NotNull(message = "Condition is required")
    private Condition condition;
    @NotBlank(message = "Location is required")
    private String location;
    @NotBlank(message = "Contact preference is required")
    private String contactPreference;

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
}