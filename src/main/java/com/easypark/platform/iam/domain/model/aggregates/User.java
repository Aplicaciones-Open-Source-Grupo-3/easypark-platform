package com.easypark.platform.iam.domain.model.aggregates;

import com.easypark.platform.iam.domain.model.entities.Role;
import com.easypark.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.HashSet;
import java.util.Set;

@Entity
public class User extends AuditableAbstractAggregateRoot<User> {

    @NotBlank
    @Size(max = 50)
    @Column(unique = true, nullable = false)
    private String username;

    @NotBlank
    @Email
    @Size(max = 100)
    @Column(unique = true, nullable = false)
    private String email;

    @NotBlank
    @Size(max = 120)
    @Column(nullable = false)
    private String password;

    @NotBlank
    @Size(max = 100)
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "business_id", nullable = false)
    private Business business;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @Column(nullable = false)
    private Boolean isActive = true;

    public User() {
    }

    public User(String username, String email, String password, String name, Business business) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.name = name;
        this.business = business;
        this.isActive = true;
    }

    public User addRole(Role role) {
        this.roles.add(role);
        return this;
    }

    // Getters
    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public Business getBusiness() {
        return business;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    // Setters
    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBusiness(Business business) {
        this.business = business;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
}

