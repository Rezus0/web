package com.example.web_2.user.profilePicture;

import com.example.web_2.baseEntity.BaseEntity;
import com.example.web_2.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "profile_picture")
public class ProfilePicture extends BaseEntity {
    private String link;
    private User user;

    protected ProfilePicture() {
    }

    public ProfilePicture(String link, User user) {
        this.link = link;
        this.user = user;
    }

    @NotNull
    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @OneToOne
    @JoinColumn(name = "user_id")
    @NotNull
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
