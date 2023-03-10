package com.works.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long aid;

    private String username;
    private String password;
    private boolean enable;

    @ManyToMany
    List<Role> roles;

}
