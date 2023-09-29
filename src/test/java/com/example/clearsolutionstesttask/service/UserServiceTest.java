package com.example.clearsolutionstesttask.service;

import com.example.clearsolutionstesttask.exception.UserNotFoundException;
import com.example.clearsolutionstesttask.model.User;
import com.example.clearsolutionstesttask.model.UserDTO;
import com.example.clearsolutionstesttask.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    public void init() {
        user = User.builder()
                .id(1L)
                .email("test@gmail.com")
                .firstName("Test first name")
                .lastName("Test last name")
                .birthDate(LocalDate.now())
                .build();
    }

    @Test
    public void findAllTest() {
        List<User> users = List.of(
                User.builder().id(1L).build(),
                User.builder().id(2L).build()
        );
        Mockito.when(userRepository.findAll()).thenReturn(users);
        List<User> expectedUsers = userService.findAll();
        Assertions.assertEquals(expectedUsers, users);
    }

    @Test
    public void findAllByBirthDateRangeTest() {
        List<User> users = List.of(
                User.builder().id(1L).build(),
                User.builder().id(2L).build()
        );
        LocalDate anyDate = Mockito.mock(LocalDate.class);

        Mockito.when(userRepository.findAllByBirthDateBetween(anyDate, anyDate))
                .thenReturn(users);
        List<User> expectedUsers = userService.findAllByBirthDateRange(anyDate, anyDate);
        Assertions.assertEquals(expectedUsers, users);
    }

    @Test
    public void givenUserObject_whenSave_thenReturnUserObject() {
        Mockito.when(userRepository.save(any())).thenReturn(user);
        User expectedUser = userService.save(User.builder().build());
        Assertions.assertEquals(expectedUser, user);
    }

    @Test
    public void givenUserDTOObject_whenSave_thenReturnUserObject() {
        Mockito.when(userRepository.save(any())).thenReturn(user);
        User expectedUser = userService.save(UserDTO.builder().build());
        Assertions.assertEquals(expectedUser, user);
    }

    @Test
    public void givenUserId_whenPatch_thenThrowNotFoundException() {
        Mockito.when(userRepository.findById(anyLong())).thenThrow(UserNotFoundException.class);
        Assertions.assertThrows(UserNotFoundException.class, () -> {
            userService.patch(anyLong(), new HashMap<>());
        });
    }

    @Test
    public void givenUserIdAndPatchData_whenPatch_thenReturnUserObject() {
        Mockito.when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
        Mockito.when(userRepository.save(any())).thenReturn(user);
        User expectedUser = userService.patch(anyLong(), new HashMap<>());
        Assertions.assertEquals(expectedUser, user);
    }

    @Test
    public void givenUserId_whenUpdate_thenThrowNotFoundException() {
        Mockito.when(userRepository.findById(anyLong())).thenThrow(UserNotFoundException.class);
        Assertions.assertThrows(UserNotFoundException.class, () -> {
            userService.update(anyLong(), UserDTO.builder().build());
        });
    }

    @Test
    public void givenUserIdAndUserDTOObject_whenUpdate_thenReturnUserObject() {
        Mockito.when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
        Mockito.when(userRepository.save(any())).thenReturn(user);
        User expectedUser = userService.update(anyLong(), UserDTO.builder().build());
        Assertions.assertEquals(expectedUser, user);
    }

    @Test
    public void deleteTest() {
        doNothing().when(userRepository).deleteById(anyLong());
        userService.deleteById(anyLong());
        verify(userRepository, times(1)).deleteById(anyLong());
    }
}
