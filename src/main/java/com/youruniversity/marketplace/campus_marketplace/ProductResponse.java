package com.youruniversity.marketpalce.campus_marketplace;
import java.math.BigDecimal;
import java.time.LocalDateTime;
public class ProductResponse{
    private Long id;
    private String title;
    private String description;
    private BigDecimal price;
    private Category category;
    private Condition condition;
    private String location;
    private String contactPreference;
    private LocalDateTime createdDate;
    private boolean available;
    private UserDTO seller;

    public static class UserDTO{
        private Long id;
        private String username;
        private String name;
        private String email;
        public Long getId(){
            return id;
        }
        public void setId(Long id){
            this.id = id;
        }
        public String getUsername(){
            return username;
        }
        public void setUsername(String username){
            this.username = username;
        }
        public String getName(){
            return name;
        }
        public void setName(String name){
            this.name = name;
        }
        public String getEmail(){
            return email;
        }
        public void setEmail(String email){
            this.email = email;
        }
    }
}