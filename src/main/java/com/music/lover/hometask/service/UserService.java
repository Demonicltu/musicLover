package com.music.lover.hometask.service;

import com.music.lover.hometask.dto.UserLoginRequest;
import com.music.lover.hometask.dto.UserLoginResponse;
import com.music.lover.hometask.dto.UserRegistrationRequest;
import com.music.lover.hometask.dto.UserRegistrationResponse;
import com.music.lover.hometask.entity.User;
import com.music.lover.hometask.exception.PasswordsDontMatchException;
import com.music.lover.hometask.exception.UserAlreadyExistsException;
import com.music.lover.hometask.exception.UserNotFoundException;

public interface UserService {

    UserRegistrationResponse registerUser(UserRegistrationRequest userRegistrationRequest)
            throws UserAlreadyExistsException, PasswordsDontMatchException;

    UserLoginResponse loginUser(UserLoginRequest userLoginRequest) throws UserNotFoundException;

    User getUserByUUID(String uuid) throws UserNotFoundException;

}
