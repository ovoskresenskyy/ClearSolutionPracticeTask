package com.example.clearsolutionstesttask.service;

import com.example.clearsolutionstesttask.dto.UserDto;
import com.example.clearsolutionstesttask.entity.User;
import com.example.clearsolutionstesttask.exception.InvalidDateRangeException;
import com.example.clearsolutionstesttask.exception.UserNotFoundException;
import com.example.clearsolutionstesttask.mapper.UserMapper;
import com.example.clearsolutionstesttask.repository.UserRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service class for managing user data.
 */
@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final UserMapper mapper;

  /**
   * Creates a new user.
   *
   * @param userDto The DTO containing user information.
   * @return The created user DTO.
   */
  public UserDto create(UserDto userDto) {
    User user = mapper.toEntity(userDto);
    return mapper.toDto(userRepository.save(user));
  }

  /**
   * Updates an existing user.
   *
   * @param id      The ID of the user to update.
   * @param userDto The DTO containing updated user information.
   * @return The updated user DTO.
   */
  public UserDto update(long id, UserDto userDto) {
    User user = findById(id);
    mapper.updateEntity(userDto, user);
    return mapper.toDto(userRepository.save(user));
  }

  /**
   * Deletes a user by ID.
   *
   * @param id The ID of the user to delete.
   */
  public void deleteById(long id) {
    userRepository.deleteById(id);
  }

  /**
   * Retrieves a list of users filtered by birthdate range.
   *
   * @param fromDate The start date of the birthdate range.
   * @param toDate   The end date of the birthdate range.
   * @return List of users within the specified birthdate range.
   * @throws InvalidDateRangeException if the provided date range is invalid.
   */
  public List<UserDto> findAllByBirthDateRange(LocalDate fromDate, LocalDate toDate) {
    if (toDate.isBefore(fromDate)) {
      throw new InvalidDateRangeException(fromDate, toDate);
    }
    return mapper.toDto(userRepository.findAllByBirthDateBetween(fromDate, toDate));
  }

  /**
   * Retrieves a user by ID.
   *
   * @param id The ID of the user to retrieve.
   * @return The user entity corresponding to the provided ID.
   * @throws UserNotFoundException if no user with the provided ID is found.
   */
  private User findById(long id) {
    return userRepository.findById(id)
        .orElseThrow(() -> new UserNotFoundException(id));
  }
}