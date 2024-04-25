package com.example.clearsolutionstesttask.util;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.core.annotation.AliasFor;

/**
 * Annotates a method or annotation to define API responses for different HTTP status codes.
 */
@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@ApiResponses({
    @ApiResponse(responseCode = "200", description = "OK"),
    @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = {
        @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))}),
    @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR", content = {
        @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))})
})
@Operation
public @interface ApiResponseUtil {

  /**
   * Summary of the operation, used for API documentation.
   */
  @AliasFor(annotation = Operation.class, attribute = "summary")
  String summary() default "";

  /**
   * Description of the operation, used for API documentation.
   */
  @AliasFor(annotation = Operation.class, attribute = "description")
  String description() default "";
}
