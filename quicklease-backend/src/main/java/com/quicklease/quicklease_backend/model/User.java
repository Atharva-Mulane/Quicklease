package com.quicklease.quicklease_backend.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
private Boolean emailVerified = false;

@Column
private String verificationToken;

@Column(length = 15)
private String phone;

@Column(length = 500)
private String address;

@Column(length = 100)
private String city;

public String getPhone() { return phone; }
public void setPhone(String phone) { this.phone = phone; }
public String getAddress() { return address; }
public void setAddress(String address) { this.address = address; }
public String getCity() { return city; }
public void setCity(String city) { this.city = city; }


public Boolean getEmailVerified() { return emailVerified; }
public void setEmailVerified(Boolean emailVerified) { this.emailVerified = emailVerified; }
public String getVerificationToken() { return verificationToken; }
public void setVerificationToken(String verificationToken) { this.verificationToken = verificationToken; }

    @ManyToMany
@JoinTable(
    name = "favorites",
    joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns = @JoinColumn(name = "item_id")
)
private Set<Item> favoriteItems = new HashSet<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    public User() {
    }

    public User(String email, String password, String fullName) {
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.role = Role.USER; // everyone registers as USER by default
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Set<Item> getFavoriteItems() {
        return favoriteItems;
    }
    
    public void setFavoriteItems(Set<Item> favoriteItems) {
        this.favoriteItems = favoriteItems;
    }
}