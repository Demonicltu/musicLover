package com.music.lover.hometask.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.ManyToMany;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.FetchType;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Artist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long amgArtistId;

    private String artistName;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "USER_ARTIST",
            joinColumns = @JoinColumn(name = "ARTIST_ID"),
            inverseJoinColumns = @JoinColumn(name = "USER_ID"))
    private Set<User> artistUsers;

    public Artist() {
        //Empty for auto init
    }

    public Artist(Long amgArtistId, String artistName, User user) {
        this.amgArtistId = amgArtistId;
        this.artistName = artistName;

        this.artistUsers = new HashSet<>();
        this.artistUsers.add(user);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAmgArtistId() {
        return amgArtistId;
    }

    public void setAmgArtistId(Long amgArtistId) {
        this.amgArtistId = amgArtistId;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public Set<User> getArtistUsers() {
        return artistUsers;
    }

    public void setArtistUsers(Set<User> artistUsers) {
        this.artistUsers = artistUsers;
    }

}
