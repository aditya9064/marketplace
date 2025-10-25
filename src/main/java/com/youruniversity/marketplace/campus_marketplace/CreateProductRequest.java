package com.youruniversity.marketplace.campus_marketplace;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import org.springframework.web.multipart.MultipartFile;

public class CreateProductRequest{
    @NotBlank(message = "Title is required")
    private String title;
    @NotBlank(message = "Description is required")
    private String description;
    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", message = "Price must be greater than or equal to 0.0")
    private BigDecimal price;
    
    @NotNull(message = "At least one image is required")
    private MultipartFile[] images;
    @NotNull(message = "Category is required")
    private String category;
    @NotNull(message = "Condition is required")
    private String condition;
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
    public String getCategory(){
        return category;
    }
    public void setCategory(String category){
        this.category = category;
    }
    public String getCondition(){
        return condition;
    }
    public void setCondition(String condition){
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

    public MultipartFile[] getImages() {
        return images;
    }

    public void setImages(MultipartFile[] images) {
        this.images = images;
    }
}