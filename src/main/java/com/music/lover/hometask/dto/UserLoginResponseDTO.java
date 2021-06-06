package com.music.lover.hometask.dto;

public class UserLoginResponseDTO {

    private String uuid;

    public UserLoginResponseDTO() {
        //Empty for auto init
    }

    public UserLoginResponseDTO(String uuid) {
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

}
