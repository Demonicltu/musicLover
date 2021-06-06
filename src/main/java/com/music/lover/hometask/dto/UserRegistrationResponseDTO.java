package com.music.lover.hometask.dto;

public class UserRegistrationResponseDTO {

    private String name;

    private String username;

    private String password;

    private String uuid;

    public UserRegistrationResponseDTO(String name, String username, String password, String uuid) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.uuid = uuid;
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

}
