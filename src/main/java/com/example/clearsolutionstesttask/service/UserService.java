package com.example.clearsolutionstesttask.service;

import com.example.clearsolutionstesttask.exception.UserNotFoundException;
import com.example.clearsolutionstesttask.model.User;
import com.example.clearsolutionstesttask.model.UserDTO;
import com.example.clearsolutionstesttask.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAllByBirthDateRange(LocalDate fromDate, LocalDate toDate) {
        return userRepository.findAllByBirthDateBetween(fromDate, toDate);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public User save(UserDTO userDTO) {
        return save(UserDTO.toUser(userDTO));
    }

    public User patch(long id, Map<String, String> patchData) {
        User user = UserDTO.patchUser(findById(id), patchData);
        return save(user);
    }

    public User update(long id, UserDTO userDTO) {
        User user = findById(id);
        return save(UserDTO.updateUser(user, userDTO));
    }

    private User findById(long id) {
        return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }

    public void deleteById(long id) {
        userRepository.deleteById(id);
    }
}