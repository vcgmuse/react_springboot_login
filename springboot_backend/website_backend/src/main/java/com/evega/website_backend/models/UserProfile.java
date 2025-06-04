package com.evega.website_backend.models;

//import com.evega.website_backend.dto.UserProfileDTO;

import jakarta.persistence.*;

@Entity
@Table(name = "user_profiles")
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    private String address;
    private String name;
    private String phone;
    private String country;
    private String state;
    private String city;
    private String zipCode;

    // Constructors, getters, and setters
    public UserProfile() {}

    public UserProfile(User user, String address, String name, String phone, String country, String state, String city, String zipCode) {
        this.user = user;
        this.address = address;
        this.name = name;
        this.phone = phone;
        this.country = country;
        this.state = state;
        this.city = city;
        this.zipCode = zipCode;
    }
    
//    public UserProfile getUserProfile(UserProfileDTO userProfileDTO) {
//    	UserProfile userProfile = new UserProfile();
//    	
//    	
//    	return userProfile;
//    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    

    public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
}