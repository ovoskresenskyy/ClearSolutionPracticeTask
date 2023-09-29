package com.example.clearsolutionstesttask.controller;

import com.example.clearsolutionstesttask.exception.InvalidDateRangeException;
import com.example.clearsolutionstesttask.model.User;
import com.example.clearsolutionstesttask.model.UserDTO;
import com.example.clearsolutionstesttask.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<User>> findAll() {
        List<User> users = userService.findAll();

        if (users.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(users);
    }

    @GetMapping("/birthDateRange")
    public ResponseEntity<List<User>> findAllByBirthDateRange(
            @RequestParam(value = "fromDate") LocalDate fromDate,
            @RequestParam(value = "toDate") LocalDate toDate)
            throws InvalidDateRangeException {

        if (toDate.isBefore(fromDate)) {
            throw new InvalidDateRangeException(fromDate, toDate);
        }

        return ResponseEntity.ok(userService.findAllByBirthDateRange(fromDate, toDate));
    }

    @PostMapping
    public ResponseEntity<User> addUser(@Valid @RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.save(userDTO));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<User> patchUser(@PathVariable long id, @RequestBody Map<String, String> patchDTO) {
        return ResponseEntity.accepted().body(userService.patch(id, patchDTO));
    }

    @PutMapping("{id}")
    public ResponseEntity<User> updateUser(@PathVariable long id, @Valid @RequestBody UserDTO userDTO) {
        return ResponseEntity.accepted().body(userService.update(id, userDTO));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<User> deleteUser(@PathVariable long id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
