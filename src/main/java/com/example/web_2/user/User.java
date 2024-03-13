package com.example.web_2.user;

import com.example.web_2.baseEntity.BaseEntity;
import com.example.web_2.offer.Offer;
import com.example.web_2.user.profilePicture.ProfilePicture;
import com.example.web_2.user.user_role.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.BatchSize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
public class User extends BaseEntity implements UserDetails {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private boolean isActive;
    private UserRole role;
    private String imageUrl;
    private List<Offer> offers;
    private ProfilePicture picture;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "User's username can't be blank")
    public String getUsername() {
        return username;
    }

    @NotBlank(message = "User's password can't be blank")
    @Size(min = 6, message = "User's password must be at least 6 characters long")
    public String getPassword() {
        return password;
    }

    @NotBlank(message = "User's first name can't be blank")
    public String getFirstName() {
        return firstName;
    }

    @NotBlank(message = "User's last name can't be blank")
    public String getLastName() {
        return lastName;
    }

    public boolean isActive() {
        return isActive;
    }

    @ManyToOne
    @JoinColumn(name = "role_id")
    @NotNull(message = "User's role can't be null")
    public UserRole getRole() {
        return role;
    }

    @NotBlank(message = "User's image URL can't be blank")
    public String getImageUrl() {
        return imageUrl;
    }

    @BatchSize(size = 10)
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, mappedBy = "seller")
    public List<Offer> getOffers() {
        return offers;
    }

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, mappedBy = "user")
    public ProfilePicture getPicture() {
        return picture;
    }

    @Transient
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Transient
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Transient
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Transient
    @Override
    public boolean isEnabled() {
        return true;
    }

    @Transient
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + getRole().getName().name()));
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setOffers(List<Offer> offers) {
        this.offers = offers;
    }

    public void setPicture(ProfilePicture picture) {
        this.picture = picture;
    }
}
