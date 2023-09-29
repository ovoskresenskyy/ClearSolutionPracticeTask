package com.example.clearsolutionstesttask.validation;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class BirthDateValidator implements ConstraintValidator<ValidBirthDate, LocalDate> {

    @Value("${user.age}")
    public int age;

    private void setViolationMessage(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation();
    }

    private boolean isAgeNotValid(LocalDate birthDate) {
        return LocalDate.now().minus(age, ChronoUnit.YEARS).isBefore(birthDate);
    }

    @Override
    public boolean isValid(LocalDate birthDate, ConstraintValidatorContext context) {
        if (birthDate == null) {
            setViolationMessage(context, "Please enter birth date.");
            return false;
        }

        if (isAgeNotValid(birthDate)) {
            setViolationMessage(context, "Available only to users over " + age + " years of age");
            return false;
        }

        return true;
    }
}
