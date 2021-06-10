package com.music.lover.hometask.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.ManyToMany;
import javax.persistence.FetchType;
import java.util.Set;
import java.util.UUID;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String username;

    private String password;

    private String uuid;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "artistUsers")
    private Set<Artist> userArtists;

    public User() {
        //Empty for auto init
    }

    public User(String name, String username, String password) {
        this.name = name;
        this.username = username;
        this.password = password;
        uuid = UUID.randomUUID().toString();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Set<Artist> getUserArtists() {
        return userArtists;
    }

    public void setUserArtists(Set<Artist> userArtists) {
        this.userArtists = userArtists;
    }

}
