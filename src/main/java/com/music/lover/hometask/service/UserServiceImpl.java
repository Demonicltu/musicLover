package com.music.lover.hometask.service;

import com.music.lover.hometask.dto.UserLoginDTO;
import com.music.lover.hometask.dto.UserLoginResponseDTO;
import com.music.lover.hometask.dto.UserRegistrationDTO;
import com.music.lover.hometask.dto.UserRegistrationResponseDTO;
import com.music.lover.hometask.entity.User;
import com.music.lover.hometask.exception.PasswordsDontMatchException;
import com.music.lover.hometask.exception.UserAlreadyExistsException;
import com.music.lover.hometask.exception.UserNotFoundException;
import com.music.lover.hometask.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserRegistrationResponseDTO registerUser(UserRegistrationDTO userRegistrationDTO)
            throws UserAlreadyExistsException, PasswordsDontMatchException {
        validateUserUniqueness(userRegistrationDTO);

        User user = new User(
                userRegistrationDTO.getName(),
                userRegistrationDTO.getUsername(),
                userRegistrationDTO.getPassword()
        );

        User savedUser = userRepository.save(user);

        return new UserRegistrationResponseDTO(
                savedUser.getName(),
                savedUser.getUsername(),
                savedUser.getPassword(),
                savedUser.getUuid()
        );
    }

    public UserLoginResponseDTO loginUser(UserLoginDTO userLoginDTO) throws UserNotFoundException {
        User authenticatedUser = userRepository.findByUsernameAndPassword(
                userLoginDTO.getUsername(),
                userLoginDTO.getPassword()
        ).orElseThrow(UserNotFoundException::new);

        return new UserLoginResponseDTO(authenticatedUser.getUuid());
    }

    public User getUserByUUID(String uuid) throws UserNotFoundException {
        return userRepository.findByUuid(uuid)
                .orElseThrow(UserNotFoundException::new);
    }

    private void validateUserUniqueness(UserRegistrationDTO user)
            throws UserAlreadyExistsException, PasswordsDontMatchException {
        if (!user.getPassword().equals(user.getRepeatPassword())) {
            throw new PasswordsDontMatchException();
        }

        if (userRepository.existsByName(user.getName())) {
            throw new UserAlreadyExistsException();
        }
    }

}
