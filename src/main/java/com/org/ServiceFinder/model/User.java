package com.org.ServiceFinder.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String password;  // REMOVE @JsonIgnore from here!
    private String phone;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String address;
    private Double latitude;
    private Double longitude;
}