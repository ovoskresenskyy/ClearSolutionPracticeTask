package com.example.clearsolutionstesttask.controller;

import com.example.clearsolutionstesttask.dto.UserDto;
import com.example.clearsolutionstesttask.service.UserService;
import com.example.clearsolutionstesttask.util.ApiResponseUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller class for handling user-related endpoints.
 */
@RestController
@RequiredArgsConstructor
@Tag(name = "User")
@RequestMapping("/users")
public class UserController {

  private final UserService userService;

  /**
   * Endpoint to create a new user.
   *
   * @param userDTO The DTO containing user information.
   * @return The created user DTO.
   */
  @PostMapping
  @ApiResponseUtil(summary = "Create a new user")
  public UserDto createUser(@Valid @RequestBody UserDto userDTO) {
    return userService.create(userDTO);
  }

  /**
   * Endpoint to update an existing user by ID.
   *
   * @param id      The ID of the user to update.
   * @param userDTO The DTO containing updated user information.
   * @return The updated user DTO.
   */
  @PutMapping("/{id}")
  @ApiResponseUtil(summary = "Update the user by ID")
  public UserDto updateUser(@PathVariable long id, @Valid @RequestBody UserDto userDTO) {
    return userService.update(id, userDTO);
  }

  /**
   * Endpoint to delete a user by ID.
   *
   * @param id The ID of the user to delete.
   */
  @DeleteMapping("{id}")
  @ApiResponseUtil(summary = "Delete the user by ID")
  public void deleteUser(@PathVariable long id) {
    userService.deleteById(id);
  }

  /**
   * Endpoint to retrieve a list of users filtered by birthdate range.
   *
   * @param fromDate The start date of the birthdate range.
   * @param toDate   The end date of the birthdate range.
   * @return List of users within the specified birthdate range.
   */
  @GetMapping("/birth-date-range")
  @ApiResponseUtil(summary = "List of users filtered by birth date range")
  public List<UserDto> findAllByBirthDateRange(
      @RequestParam(value = "fromDate") LocalDate fromDate,
      @RequestParam(value = "toDate") LocalDate toDate) {
    return userService.findAllByBirthDateRange(fromDate, toDate);
  }
}
