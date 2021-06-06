package com.music.lover.hometask.service;

import com.music.lover.hometask.dto.UserLoginDTO;
import com.music.lover.hometask.dto.UserLoginResponseDTO;
import com.music.lover.hometask.dto.UserRegistrationDTO;
import com.music.lover.hometask.dto.UserRegistrationResponseDTO;
import com.music.lover.hometask.entity.User;
import com.music.lover.hometask.exception.PasswordsDontMatchException;
import com.music.lover.hometask.exception.UserAlreadyExistsException;
import com.music.lover.hometask.exception.UserNotFoundException;

public interface UserService {

    UserRegistrationResponseDTO registerUser(UserRegistrationDTO userRegistrationDTO) throws UserAlreadyExistsException, PasswordsDontMatchException;

    UserLoginResponseDTO loginUser(UserLoginDTO userLoginDTO) throws UserNotFoundException;

    User getUserByUUID(String uuid) throws UserNotFoundException;

}
