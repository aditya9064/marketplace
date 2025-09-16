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
}