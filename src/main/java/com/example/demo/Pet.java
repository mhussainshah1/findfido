package com.example.demo;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;


@Entity
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    @Size(min = 1)
    private String name;

    @NotNull
    @Size(min = 1)
    private String datelost;

    private String image;

    private String description;

    private String status;

    @ManyToMany(mappedBy = "pets", fetch = FetchType.EAGER)
    private Set<User> users;

    public Pet() {
        image = "";
        users = new HashSet<>();
    }

    public Pet(@NotNull @Size(min = 1) String name, @NotNull @Size(min = 1) String datelost, String image, String description, String status) {
        this();
        this.name = name;
        this.datelost = datelost;
        this.image = image;
        this.description = description;
        this.status = status;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDatelost() {
        return datelost;
    }

    public void setDatelost(String datelost) {
        this.datelost = datelost;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }
}
