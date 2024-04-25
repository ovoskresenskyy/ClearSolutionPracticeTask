package com.example.clearsolutionstesttask.util.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation should be applied to fields representing birthdate.
 */
@Documented
@Constraint(validatedBy = BirthDateValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidBirthDate {

  String message() default "Invalid birth date";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}