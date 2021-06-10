package com.music.lover.hometask.controller;

import com.music.lover.hometask.dto.UserLoginRequest;
import com.music.lover.hometask.dto.UserLoginResponse;
import com.music.lover.hometask.dto.UserRegistrationRequest;
import com.music.lover.hometask.dto.UserRegistrationResponse;
import com.music.lover.hometask.exception.PasswordsDontMatchException;
import com.music.lover.hometask.exception.RequestException;
import com.music.lover.hometask.exception.UserAlreadyExistsException;
import com.music.lover.hometask.exception.UserNotFoundException;
import com.music.lover.hometask.exception.error.ApplicationError;
import com.music.lover.hometask.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserRegistrationResponse registerUser(@RequestBody @Valid UserRegistrationRequest userRegistrationRequest) {
        try {
            return userService.registerUser(userRegistrationRequest);
        } catch (PasswordsDontMatchException e) {
            throw new RequestException(ApplicationError.PASSWORDS_DONT_MATCH);
        } catch (UserAlreadyExistsException e) {
            throw new RequestException(ApplicationError.USER_ALREADY_EXISTS);
        }
    }

    @PostMapping("/login")
    public UserLoginResponse loginUser(@RequestBody @Valid UserLoginRequest userLoginRequest) {
        try {
            return userService.loginUser(userLoginRequest);
        } catch (UserNotFoundException e) {
            throw new RequestException(ApplicationError.USER_NOT_FOUND);
        }
    }

}
