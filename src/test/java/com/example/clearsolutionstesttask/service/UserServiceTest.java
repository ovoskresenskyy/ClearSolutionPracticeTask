package com.example.clearsolutionstesttask.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.clearsolutionstesttask.dto.UserDto;
import com.example.clearsolutionstesttask.entity.User;
import com.example.clearsolutionstesttask.exception.InvalidDateRangeException;
import com.example.clearsolutionstesttask.exception.UserNotFoundException;
import com.example.clearsolutionstesttask.mapper.UserMapper;
import com.example.clearsolutionstesttask.repository.UserRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Unit tests for the UserService class.
 */
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

  @Mock
  private UserRepository userRepository;
  @Mock
  private UserMapper mapper;

  @InjectMocks
  private UserService userService;

  private final long userId = 999L;
  private User user;
  private UserDto userDto;

  @BeforeEach
  public void init() {
    String testEmail = "test@gmail.com";

    user = User.builder()
        .id(userId)
        .email(testEmail)
        .birthDate(LocalDate.now())
        .build();

    userDto = UserDto.builder()
        .email(testEmail)
        .birthDate(LocalDate.now())
        .build();
  }

  @Test
  public void testCreate() {
    when(mapper.toEntity(any(UserDto.class))).thenReturn(user);
    when(userRepository.save(any())).thenReturn(user);
    when(mapper.toDto(any(User.class))).thenReturn(userDto);

    UserDto resultDto = userService.create(userDto);

    assertEquals(userDto, resultDto);
  }

  @Test
  public void testUpdate_returnsUserDto() {
    when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
    doNothing().when(mapper).updateEntity(any(UserDto.class), any(User.class));
    when(userRepository.save(any())).thenReturn(user);
    when(mapper.toDto(any(User.class))).thenReturn(userDto);

    UserDto resultDto = userService.update(userId, userDto);

    assertEquals(userDto, resultDto);
  }

  @Test
  public void testUpdate_throwsUserNotFoundException() {
    when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

    assertThrows(UserNotFoundException.class,
        () -> userService.update(userId, userDto));
  }

  @Test
  public void testDelete() {
    doNothing().when(userRepository).deleteById(anyLong());

    userService.deleteById(userId);

    verify(userRepository, times(1)).deleteById(anyLong());
  }

  @Test
  void findAllByBirthDateRange_ValidDateRange_ReturnsUserDtoList() {
    List<User> users = List.of(user);
    List<UserDto> userDtos = List.of(userDto);
    LocalDate validFromDate = LocalDate.of(1990, 1, 1);
    LocalDate validToDate = LocalDate.of(2000, 1, 1);

    when(userRepository.findAllByBirthDateBetween(any(), any())).thenReturn(users);
    when(mapper.toDto(anyList())).thenReturn(userDtos);

    List<UserDto> result = userService.findAllByBirthDateRange(validFromDate, validToDate);

    assertEquals(userDtos, result);
  }

  @Test
  void findAllByBirthDateRange_InvalidDateRange_ThrowsInvalidDateRangeException() {
    LocalDate invalidFromDate = LocalDate.of(2000, 1, 1);
    LocalDate invalidToDate = LocalDate.of(1990, 1, 1);

    assertThrows(InvalidDateRangeException.class,
        () -> userService.findAllByBirthDateRange(invalidFromDate, invalidToDate));
  }
}
