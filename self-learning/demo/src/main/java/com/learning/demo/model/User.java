package com.learning.demo.model;

import jakarta.persistence.*;
import jdk.jfr.Enabled;
import lombok.Getter;
import lombok.Setter;

import javax.management.ConstructorParameters;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter

@Table(name="user")
public class User {
    @Id
    @Column(name = "userid")
    private int userid;

    @Column(unique = true, name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "userid"))
    @Column(name = "role")
    private Set<String> roles = new HashSet<>();


    public User() {
    }

    public User(int userid, String username, String password, Set<String> roles) {
        this.userid = userid;
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "User{" +
                "userid=" + userid +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", roles=" + roles +
                '}';
    }
}

