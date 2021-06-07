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
        UserRegistrationDTO userRegistrationDTO = getUserRegistrationDTO("TestPassword", "TestPassword");

        when(userRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(userRepository.existsByName(any()))
                .thenAnswer(invocation -> false);

        UserRegistrationResponseDTO userRegistrationResponseDTO = userService.registerUser(userRegistrationDTO);

        verify(userRepository, times(1))
                .save(any());

        verify(userRepository, times(1))
                .existsByName(any());

        Assertions.assertEquals(userRegistrationDTO.getName(), userRegistrationResponseDTO.getName());
        Assertions.assertEquals(userRegistrationDTO.getUsername(), userRegistrationResponseDTO.getUsername());
        Assertions.assertEquals(userRegistrationDTO.getPassword(), userRegistrationResponseDTO.getPassword());
        Assertions.assertEquals(userRegistrationDTO.getRepeatPassword(), userRegistrationResponseDTO.getPassword());
    }

    @Test
    void testRegisterUserPasswordsDontMatchException() {
        UserRegistrationDTO userRegistrationDTO = getUserRegistrationDTO("1111111111", "2222222222");

        Assertions.assertThrows(PasswordsDontMatchException.class, () -> userService.registerUser(userRegistrationDTO));

        verify(userRepository, times(0))
                .save(any());

        verify(userRepository, times(0))
                .existsByName(any());
    }

    @Test
    void testRegisterUserUserAlreadyExistsException() {
        UserRegistrationDTO userRegistrationDTO = getUserRegistrationDTO("TestPassword", "TestPassword");

        when(userRepository.existsByName(any()))
                .thenAnswer(invocation -> true);

        Assertions.assertThrows(UserAlreadyExistsException.class, () -> userService.registerUser(userRegistrationDTO));

        verify(userRepository, times(0))
                .save(any());

        verify(userRepository, times(1))
                .existsByName(any());
    }

    @Test
    void testLoginUser() throws UserNotFoundException {
        UserLoginDTO userLoginDTO = getUserLoginDTO();

        when(userRepository.findByUsernameAndPassword(any(), any()))
                .thenAnswer(invocation -> {
                    User user = new User();
                    user.setUuid(UUID.randomUUID().toString());

                    return Optional.of(user);
                });

        UserLoginResponseDTO userLoginResponseDTO = userService.loginUser(userLoginDTO);

        verify(userRepository, times(1))
                .findByUsernameAndPassword(any(), any());

        Assertions.assertFalse(userLoginResponseDTO.getUuid().isEmpty());
    }

    @Test
    void testLoginUserUserNotFoundException() {
        UserLoginDTO userLoginDTO = getUserLoginDTO();

        when(userRepository.findByUsernameAndPassword(any(), any()))
                .thenAnswer(invocation -> Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () -> userService.loginUser(userLoginDTO));

        verify(userRepository, times(1))
                .findByUsernameAndPassword(any(), any());
    }

    @Test
    void testGetUserByUUID() throws UserNotFoundException {
        String uuid = UUID.randomUUID().toString();

        when(userRepository.findByUuid(any()))
                .thenAnswer(invocation -> {
                    User user = new User();
                    user.setUuid(UUID.randomUUID().toString());

                    return Optional.of(user);
                });

        User user = userService.getUserByUUID(uuid);

        verify(userRepository, times(1))
                .findByUuid(any());

        Assertions.assertNotNull(user);
        Assertions.assertFalse(user.getUuid().isEmpty());
    }

    @Test
    void testGetUserByUUIDUserNotFoundException() {
        String uuid = UUID.randomUUID().toString();

        when(userRepository.findByUuid(any()))
                .thenAnswer(invocation -> Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () -> userService.getUserByUUID(uuid));

        verify(userRepository, times(1))
                .findByUuid(any());
    }

    private UserRegistrationDTO getUserRegistrationDTO(String password, String repeatPassword) {
        UserRegistrationDTO userRegistrationDTO = new UserRegistrationDTO();
        userRegistrationDTO.setName("TestName");
        userRegistrationDTO.setUsername("TestUsername");
        userRegistrationDTO.setPassword(password);
        userRegistrationDTO.setRepeatPassword(repeatPassword);

        return userRegistrationDTO;
    }

    private UserLoginDTO getUserLoginDTO() {
        UserLoginDTO userLoginDTO = new UserLoginDTO();
        userLoginDTO.setUsername("TestUsername");
        userLoginDTO.setPassword("TestPassword");

        return userLoginDTO;
    }

}
