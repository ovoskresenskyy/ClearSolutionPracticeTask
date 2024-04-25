package com.example.clearsolutionstesttask.exception;

/**
 * Exception thrown when a user with a specific ID is not found.
 */
public class UserNotFoundException extends RuntimeException {

  public UserNotFoundException(long id) {
    super("User with id %s not found".formatted(id));
  }
}