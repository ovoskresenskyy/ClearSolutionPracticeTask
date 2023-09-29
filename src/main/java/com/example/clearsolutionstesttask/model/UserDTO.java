package com.example.clearsolutionstesttask.model;

import com.example.clearsolutionstesttask.validation.ValidBirthDate;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Map;

@Data
@Builder
public class UserDTO {

    @NotBlank(message = "Please enter email.")
    @Email(message = "Invalid email.")
    private String email;
    @NotBlank(message = "Please enter first name.")
    private String firstName;
    @NotBlank(message = "Please enter last name.")
    private String lastName;
    @ValidBirthDate
    private LocalDate birthDate;
    private String address;
    private String phoneNumber;

    public static User toUser(UserDTO userDto) {
        return updateUser(User.builder().build(), userDto);
    }

    public static User patchUser(User user, Map<String, String> patchData) {
        UserDTO userDTO = UserDTO.builder()
                .email(patchData.getOrDefault("email", user.getEmail()))
                .firstName(patchData.getOrDefault("firstName", user.getFirstName()))
                .lastName(patchData.getOrDefault("lastName", user.getLastName()))
                .birthDate(LocalDate.parse(patchData.getOrDefault("birthDate", user.getBirthDate().toString())))
                .address(patchData.getOrDefault("address", user.getAddress()))
                .phoneNumber(patchData.getOrDefault("phoneNumber", user.getPhoneNumber()))
                .build();

        return updateUser(user, userDTO);
    }

    public static User updateUser(User user, UserDTO userDto) {
        user.setEmail(userDto.email);
        user.setFirstName(userDto.firstName);
        user.setLastName(userDto.lastName);
        user.setBirthDate(userDto.birthDate);
        user.setAddress(userDto.address);
        user.setPhoneNumber(userDto.phoneNumber);
        return user;
    }
}
