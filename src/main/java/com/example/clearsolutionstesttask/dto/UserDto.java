package com.example.clearsolutionstesttask.dto;

import com.example.clearsolutionstesttask.util.validation.ValidBirthDate;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.AccessMode;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Data Transfer Object (DTO) representing a user.
 */
@Builder
@Getter
@EqualsAndHashCode
public class UserDto {

  @Schema(accessMode = AccessMode.READ_ONLY)
  private long id;

  @Email(message = "Invalid email.")
  @NotBlank(message = "Please enter email.")
  @Schema(requiredMode = RequiredMode.REQUIRED)
  private String email;

  @NotBlank(message = "Please enter first name.")
  @Schema(requiredMode = RequiredMode.REQUIRED)
  private String firstName;

  @NotBlank(message = "Please enter last name.")
  @Schema(requiredMode = RequiredMode.REQUIRED)
  private String lastName;

  @ValidBirthDate
  @Schema(requiredMode = RequiredMode.REQUIRED)
  private LocalDate birthDate;

  @Schema(requiredMode = RequiredMode.NOT_REQUIRED)
  private String address;

  @Schema(requiredMode = RequiredMode.NOT_REQUIRED)
  private String phoneNumber;
}
