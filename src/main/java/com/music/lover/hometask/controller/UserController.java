package com.music.lover.hometask.controller;

import com.music.lover.hometask.dto.UserLoginDTO;
import com.music.lover.hometask.dto.UserLoginResponseDTO;
import com.music.lover.hometask.dto.UserRegistrationDTO;
import com.music.lover.hometask.dto.UserRegistrationResponseDTO;
import com.music.lover.hometask.exception.PasswordsDontMatchException;
import com.music.lover.hometask.exception.RequestException;
import com.music.lover.hometask.exception.UserAlreadyExistsException;
import com.music.lover.hometask.exception.UserNotFoundException;
import com.music.lover.hometask.exception.error.ApplicationError;
import com.music.lover.hometask.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(
            UserService userService
    ) {
        this.userService = userService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserRegistrationResponseDTO registerUser(@RequestBody @Valid UserRegistrationDTO userRegistrationDTO) {
        try {
            return userService.registerUser(userRegistrationDTO);
        } catch (PasswordsDontMatchException e) {
            throw new RequestException(ApplicationError.PASSWORDS_DONT_MATCH);
        } catch (UserAlreadyExistsException e) {
            throw new RequestException(ApplicationError.USER_ALREADY_EXISTS);
        }
    }

    @PostMapping("/login")
    public UserLoginResponseDTO loginUser(@RequestBody @Valid UserLoginDTO userLoginDTO) {
        try {
            return userService.loginUser(userLoginDTO);
        } catch (UserNotFoundException e) {
            throw new RequestException(ApplicationError.USER_NOT_FOUND);
        }
    }

}
