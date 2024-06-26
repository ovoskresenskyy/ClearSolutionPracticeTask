package com.example.clearsolutionstesttask.exception;

import java.time.LocalDate;

/**
 * Exception thrown when an invalid date range is provided.
 */
public class InvalidDateRangeException extends RuntimeException {

    public InvalidDateRangeException(LocalDate from, LocalDate to) {
        super("Invalid date range, %s is more then %s".formatted(from.toString(), to.toString()));
    }
}
