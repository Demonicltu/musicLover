package com.music.lover.hometask.data;

import com.music.lover.hometask.dto.UserLoginRequest;
import com.music.lover.hometask.dto.UserRegistrationRequest;
import com.music.lover.hometask.dto.UserRegistrationResponse;
import com.music.lover.hometask.entity.User;

import java.util.UUID;

public class UserMock {

    public static UserRegistrationRequest getUserRegistrationDTO() {
        return new UserRegistrationRequest(
                "TestName",
                "TestUsername",
                "TestPassword",
                "TestPassword"
        );
    }

    public static User getUser() {
        User user = new User();
        user.setUuid(UUID.randomUUID().toString());

        return user;
    }

    public static UserRegistrationRequest getUserRegistrationDTONotMatchingPasswords() {
        return new UserRegistrationRequest(
                "TestName",
                "TestUsername",
                "11111111",
                "22222222"
        );
    }

    public static UserLoginRequest getUserLoginDTO() {
        return new UserLoginRequest(
                "TestUsername",
                "TestPassword"
        );
    }

    public static UserRegistrationResponse getUserRegistrationResponse() {
        return new UserRegistrationResponse(
                "TestName",
                "TestUsername",
                "TestPassword",
                UUID.randomUUID().toString()
        );
    }

}
