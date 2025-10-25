package com.youruniversity.marketplace.campus_marketplace;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false)
    private String password;
    private String name;
    private boolean isVerified;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserFavorites> favorites = new ArrayList<>();
    
    @Column(name = "email_verified")
    private boolean emailVerified = false;
    
    @Column(name = "verification_token")
    private String verificationToken;
    public Long getId() {
         return id;
        }
    public void setId(Long id) {
        this.id = id;
    }
    public String getEmail(){
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword(){
        return password;
    }
    public void setPassword(String password){
        this.password = password;
    }
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public boolean isVerified() {
        return isVerified;
    }
    
    public void setVerified(boolean verified) {
        this.isVerified = verified;
    }
    
    public List<UserFavorites> getFavorites() {
        return favorites;
    }
    
    public void setFavorites(List<UserFavorites> favorites) {
        this.favorites = favorites;
    }
    
    public boolean isEmailVerified() {
        return emailVerified;
    }
    
    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public String getVerificationToken() {
        return verificationToken;
    }

    public void setVerificationToken(String verificationToken) {
        this.verificationToken = verificationToken;
    }
}
 
