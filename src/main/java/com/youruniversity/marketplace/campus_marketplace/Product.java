package com.youruniversity.marketplace.campus_marketplace;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
public class Product{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Condition condition;

    @Column(nullable = false)
    private String location;

    @Column(name = "contact_preference")
    private String contactPreference;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(nullable = false)
    private boolean available = true;

    @ManyToOne
    @JoinColumn(name = "seller_id", nullable = false)
    private User seller;    
}

public Product(){
}

public Long getId(){
    return id;
}
public void setId(Long id){
    this.id = id;
}
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
public LocalDateTime getCreatedDate(){
    return createdDate;
}
public void setCreatedDate(LocalDateTime createdDate){
    this.createdDate = createdDate;
}
public boolean isAvailable(){
    return available;
}
public void setAvailable(boolean available){
    this.available = available;
}
public User getSeller(){
    return seller;
}
public void setSeller(User seller){
    this.seller = seller;
}