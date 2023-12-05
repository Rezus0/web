package com.example.web_2.user.user_role;

import com.example.web_2.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.BatchSize;

import java.util.List;

@Entity
@Table(name = "roles")
public class UserRole extends com.example.web_2.baseEntity.Entity {
    private Role name;
    private List<User> users;

    @Enumerated
    @NotNull(message = "Role's name can't be null")
    public Role getName() {
        return name;
    }

    @BatchSize(size = 10)
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, mappedBy = "role")
    public List<User> getUsers() {
        return users;
    }

    public void setName(Role name) {
        this.name = name;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
