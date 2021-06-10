package com.music.lover.hometask.mapper;

import com.music.lover.hometask.dto.UserRegistrationRequest;
import com.music.lover.hometask.dto.UserRegistrationResponse;
import com.music.lover.hometask.entity.User;

public class UserMapper {

    public static UserRegistrationResponse toUserRegistrationResponse(User user) {
        return new UserRegistrationResponse(
                user.getName(),
                user.getUsername(),
                user.getPassword(),
                user.getUuid()
        );
    }

    public static User toUserRegistrationResponse(UserRegistrationRequest userRegistrationRequest) {
        return new User(
                userRegistrationRequest.getName(),
                userRegistrationRequest.getUsername(),
                userRegistrationRequest.getPassword()
        );
    }

}
