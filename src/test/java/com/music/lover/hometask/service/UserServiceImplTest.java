package com.music.lover.hometask.service;

import com.music.lover.hometask.data.UserMock;
import com.music.lover.hometask.dto.UserLoginResponse;
import com.music.lover.hometask.dto.UserRegistrationRequest;
import com.music.lover.hometask.dto.UserRegistrationResponse;
import com.music.lover.hometask.entity.User;
import com.music.lover.hometask.exception.PasswordsDontMatchException;
import com.music.lover.hometask.exception.UserAlreadyExistsException;
import com.music.lover.hometask.exception.UserNotFoundException;
import com.music.lover.hometask.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Test
    void testRegisterUser() throws UserAlreadyExistsException, PasswordsDontMatchException {
        UserRegistrationRequest userRegistrationRequest = UserMock.getUserRegistrationDTO();

        when(userRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(userRepository.existsByName(any()))
                .thenAnswer(invocation -> false);

        UserRegistrationResponse userRegistrationResponse = userService.registerUser(userRegistrationRequest);

        verify(userRepository, times(1))
                .save(any());

        verify(userRepository, times(1))
                .existsByName(any());

        Assertions.assertEquals(userRegistrationRequest.getName(), userRegistrationResponse.getName());
        Assertions.assertEquals(userRegistrationRequest.getUsername(), userRegistrationResponse.getUsername());
        Assertions.assertEquals(userRegistrationRequest.getPassword(), userRegistrationResponse.getPassword());
        Assertions.assertEquals(userRegistrationRequest.getRepeatPassword(), userRegistrationResponse.getPassword());
    }

    @Test
    void testRegisterUserPasswordsDontMatchException() {
        Assertions.assertThrows(PasswordsDontMatchException.class, () ->
                userService.registerUser(UserMock.getUserRegistrationDTONotMatchingPasswords()));

        verify(userRepository, times(0))
                .save(any());

        verify(userRepository, times(0))
                .existsByName(any());
    }

    @Test
    void testRegisterUserUserAlreadyExistsException() {
        when(userRepository.existsByName(any()))
                .thenAnswer(invocation -> true);

        Assertions.assertThrows(UserAlreadyExistsException.class, () ->
                userService.registerUser(UserMock.getUserRegistrationDTO()));

        verify(userRepository, times(0))
                .save(any());

        verify(userRepository, times(1))
                .existsByName(any());
    }

    @Test
    void testLoginUser() throws UserNotFoundException {
        when(userRepository.findByUsernameAndPassword(any(), any()))
                .thenAnswer(invocation -> {
                    User user = new User();
                    user.setUuid(UUID.randomUUID().toString());

                    return Optional.of(user);
                });

        UserLoginResponse userLoginResponse = userService.loginUser(UserMock.getUserLoginDTO());

        verify(userRepository, times(1))
                .findByUsernameAndPassword(any(), any());

        Assertions.assertFalse(userLoginResponse.getUuid().isEmpty());
    }

    @Test
    void testLoginUserUserNotFoundException() {
        when(userRepository.findByUsernameAndPassword(any(), any()))
                .thenAnswer(invocation -> Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () -> userService.loginUser(UserMock.getUserLoginDTO()));

        verify(userRepository, times(1))
                .findByUsernameAndPassword(any(), any());
    }

    @Test
    void testGetUserByUUID() throws UserNotFoundException {
        when(userRepository.findByUuid(any()))
                .thenAnswer(invocation -> {
                    User user = new User();
                    user.setUuid(invocation.getArgument(0));

                    return Optional.of(user);
                });

        User user = userService.getUserByUUID(UUID.randomUUID().toString());

        verify(userRepository, times(1))
                .findByUuid(any());

        Assertions.assertNotNull(user);
        Assertions.assertFalse(user.getUuid().isEmpty());
    }

    @Test
    void testGetUserByUUIDUserNotFoundException() {
        when(userRepository.findByUuid(any()))
                .thenAnswer(invocation -> Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () -> userService.getUserByUUID(UUID.randomUUID().toString()));

        verify(userRepository, times(1))
                .findByUuid(any());
    }

}
