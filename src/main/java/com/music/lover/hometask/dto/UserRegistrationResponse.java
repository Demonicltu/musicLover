package com.music.lover.hometask.dto;

public class UserRegistrationResponse {

    private final String name;

    private final String username;

    private final String password;

    private final String uuid;

    public UserRegistrationResponse(String name, String username, String password, String uuid) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getUuid() {
        return uuid;
    }

}
