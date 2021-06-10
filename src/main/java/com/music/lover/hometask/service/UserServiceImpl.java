package com.music.lover.hometask.service;

import com.music.lover.hometask.dto.UserLoginRequest;
import com.music.lover.hometask.dto.UserLoginResponse;
import com.music.lover.hometask.dto.UserRegistrationRequest;
import com.music.lover.hometask.dto.UserRegistrationResponse;
import com.music.lover.hometask.entity.User;
import com.music.lover.hometask.exception.PasswordsDontMatchException;
import com.music.lover.hometask.exception.UserAlreadyExistsException;
import com.music.lover.hometask.exception.UserNotFoundException;
import com.music.lover.hometask.mapper.UserMapper;
import com.music.lover.hometask.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserRegistrationResponse registerUser(UserRegistrationRequest userRegistrationRequest)
            throws UserAlreadyExistsException, PasswordsDontMatchException {
        validateUserUniqueness(userRegistrationRequest);

        User user = UserMapper.toUserRegistrationResponse(userRegistrationRequest);
        User savedUser = userRepository.save(user);

        return UserMapper.toUserRegistrationResponse(savedUser);
    }

    public UserLoginResponse loginUser(UserLoginRequest userLoginRequest) throws UserNotFoundException {
        User authenticatedUser = userRepository.findByUsernameAndPassword(
                userLoginRequest.getUsername(),
                userLoginRequest.getPassword()
        ).orElseThrow(UserNotFoundException::new);

        return new UserLoginResponse(authenticatedUser.getUuid());
    }

    public User getUserByUUID(String uuid) throws UserNotFoundException {
        return userRepository.findByUuid(uuid)
                .orElseThrow(UserNotFoundException::new);
    }

    private void validateUserUniqueness(UserRegistrationRequest user)
            throws UserAlreadyExistsException, PasswordsDontMatchException {
        if (!user.getPassword().equals(user.getRepeatPassword())) {
            throw new PasswordsDontMatchException();
        }

        if (userRepository.existsByName(user.getName())) {
            throw new UserAlreadyExistsException();
        }
    }

}
