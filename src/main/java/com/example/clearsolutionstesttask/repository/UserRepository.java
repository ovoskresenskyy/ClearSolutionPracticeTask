package com.example.clearsolutionstesttask.repository;

import com.example.clearsolutionstesttask.entity.User;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for accessing user data from the database.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  /**
   * Retrieves a list of users whose birthdate fall within the specified range.
   *
   * @param fromDate The start date of the birthdate range.
   * @param toDate   The end date of the birthdate range.
   * @return List of users within the specified birthdate range.
   */
  List<User> findAllByBirthDateBetween(LocalDate fromDate, LocalDate toDate);
}
