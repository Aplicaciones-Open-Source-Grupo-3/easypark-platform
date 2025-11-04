package com.easypark.platform.iam.domain.model.entities;

import com.easypark.platform.iam.domain.model.valueobjects.Roles;
import jakarta.persistence.*;

@Entity
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, unique = true, nullable = false)
    private Roles name;

    public Role() {
    }

    public Role(Roles name) {
        this.name = name;
    }

    public String getStringName() {
        return name.name();
    }

    public static Role getDefaultRole() {
        return new Role(Roles.ROLE_OPERATOR);
    }

    public static Role toRoleFromName(String name) {
        return new Role(Roles.valueOf(name));
    }

    // Getters
    public Long getId() {
        return id;
    }

    public Roles getName() {
        return name;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setName(Roles name) {
        this.name = name;
    }
}

