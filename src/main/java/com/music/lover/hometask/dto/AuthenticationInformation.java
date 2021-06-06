package com.music.lover.hometask.dto;

import com.music.lover.hometask.entity.User;

public class AuthenticationInformation {

    private User user;

    public AuthenticationInformation(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
