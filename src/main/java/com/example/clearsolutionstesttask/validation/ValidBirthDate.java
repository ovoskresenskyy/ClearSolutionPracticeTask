package com.example.clearsolutionstesttask.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = BirthDateValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidBirthDate {

    String message() default "Invalid birth date";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}