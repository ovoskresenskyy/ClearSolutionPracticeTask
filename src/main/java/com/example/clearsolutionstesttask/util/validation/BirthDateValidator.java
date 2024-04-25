package com.example.clearsolutionstesttask.util.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Value;

/**
 * Validator for validating birth dates.
 */
public class BirthDateValidator implements ConstraintValidator<ValidBirthDate, LocalDate> {

  @Value("${user.min.age}")
  public int age;

  @Override
  public boolean isValid(LocalDate birthDate, ConstraintValidatorContext context) {
    if (birthDate == null) {
      setViolationMessage(context, "Please enter birthdate.");
      return false;
    }

    if (isAgeNotValid(birthDate)) {
      setViolationMessage(context, "Available only to users over " + age + " years of age");
      return false;
    }

    return true;
  }

  /**
   * Sets a custom violation message for a constraint validation context.
   *
   * @param context The constraint validator context.
   * @param message The custom violation message.
   */
  private void setViolationMessage(ConstraintValidatorContext context, String message) {
    context.disableDefaultConstraintViolation();
    context.buildConstraintViolationWithTemplate(message)
        .addConstraintViolation();
  }

  /**
   * Checks if the age calculated from the given birthdate is valid.
   *
   * @param birthDate The birthdate to calculate the age from.
   * @return {@code true} if the age is not valid, {@code false} otherwise.
   */
  private boolean isAgeNotValid(LocalDate birthDate) {
    return LocalDate.now().minusYears(age).isBefore(birthDate);
  }
}
