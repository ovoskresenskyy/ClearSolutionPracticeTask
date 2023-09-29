package com.example.clearsolutionstesttask.controller;

import com.example.clearsolutionstesttask.exception.InvalidDateRangeException;
import com.example.clearsolutionstesttask.exception.UserNotFoundException;
import com.example.clearsolutionstesttask.model.User;
import com.example.clearsolutionstesttask.model.UserDTO;
import com.example.clearsolutionstesttask.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    private static final String END_POINT_PATH = "/users";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    private List<User> users;

    private String testEmail;
    private String testFirstName;
    private String testLastName;
    private LocalDate suitableBirthDate;
    private LocalDate nonSuitableBirthDate;

    @Value("${user.age}")
    private int age;

    @BeforeEach
    public void init() {
        testEmail = "test@gmail.com";
        testFirstName = "Test first name";
        testLastName = "Test last name";
        suitableBirthDate = LocalDate.parse("1990-01-01");
        nonSuitableBirthDate = LocalDate.parse("2020-01-01");

        User testUser1 = User.builder()
                .id(1L)
                .email(testEmail)
                .firstName(testFirstName)
                .lastName(testLastName)
                .birthDate(suitableBirthDate)
                .build();

        User testUser2 = User.builder()
                .id(2L)
                .email("usertest2@gmail.com")
                .firstName("")
                .lastName("test2_first_name")
                .birthDate(LocalDate.parse("2010-01-01"))
                .build();

        users = List.of(testUser1, testUser2);
    }

    @Test
    public void testListShouldReturn204NoContent() throws Exception {
        Mockito.when(userService.findAll()).thenReturn(new ArrayList<>());

        mockMvc.perform(get(END_POINT_PATH))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    public void testListShouldReturn200OK() throws Exception {
        Mockito.when(userService.findAll()).thenReturn(users);

        mockMvc.perform(get(END_POINT_PATH))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$[0].email").value(testEmail))
                .andExpect(jsonPath("$[1].email").value("usertest2@gmail.com"))
                .andDo(print());
    }

    @Test
    public void testListWithBirthDateRangeShouldReturn200OK() throws Exception {
        String fromDate = "2000-01-01";
        String toDate = "2020-01-01";

        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("fromDate", fromDate);
        requestParams.add("toDate", toDate);

        Mockito.when(userService.findAllByBirthDateRange(LocalDate.parse(fromDate), LocalDate.parse(toDate)))
                .thenReturn(users);

        mockMvc.perform(get(END_POINT_PATH + "/birthDateRange")
                        .params(requestParams))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andDo(print());
    }

    @Test
    public void testListWithBirthDateRangeShouldReturn400BadRequest() throws Exception {
        String fromDate = "2030-01-01";
        String toDate = "2020-01-01";

        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("fromDate", fromDate);
        requestParams.add("toDate", toDate);

        Mockito.when(userService.findAllByBirthDateRange(
                        LocalDate.parse(fromDate), LocalDate.parse(toDate)))
                .thenThrow(InvalidDateRangeException.class);

        mockMvc.perform(get(END_POINT_PATH + "/birthDateRange")
                        .params(requestParams))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"))
                .andDo(print());
    }

    @Test
    public void testAddShouldReturn400BadRequest() throws Exception {
        UserDTO testUserDTO = UserDTO.builder()
                .email("")
                .firstName("")
                .lastName("")
                .build();

        mockMvc.perform(post(END_POINT_PATH)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(testUserDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(4)))
                .andExpect(jsonPath("$.errors", hasItem("Please enter email.")))
                .andExpect(jsonPath("$.errors", hasItem("Please enter first name.")))
                .andExpect(jsonPath("$.errors", hasItem("Please enter last name.")))
                .andExpect(jsonPath("$.errors", hasItem("Please enter birth date.")))
                .andDo(print());
    }

    @Test
    public void testAgeShouldReturn400BadRequest() throws Exception {
        UserDTO testUserDTO = UserDTO.builder()
                .email(testEmail)
                .firstName(testFirstName)
                .lastName(testLastName)
                .birthDate(nonSuitableBirthDate)
                .build();

        mockMvc.perform(post(END_POINT_PATH)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(testUserDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors", hasItem("Available only to users over " + age + " years of age")))
                .andDo(print());
    }

    @Test
    public void testAddShouldReturn200OK() throws Exception {
        UserDTO testUserDTO = UserDTO.builder()
                .email(testEmail)
                .firstName(testFirstName)
                .lastName(testLastName)
                .birthDate(suitableBirthDate)
                .build();

        Mockito.when(userService.save(testUserDTO)).thenReturn(User.builder().build());

        mockMvc.perform(post(END_POINT_PATH)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(testUserDTO)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void testUpdateShouldReturn404NotFound() throws Exception {
        long userId = 999L;
        UserDTO testUserDTO = UserDTO.builder()
                .email(testEmail)
                .firstName(testFirstName)
                .lastName(testLastName)
                .birthDate(suitableBirthDate)
                .build();

        Mockito.when(userService.update(userId, testUserDTO)).thenThrow(UserNotFoundException.class);

        mockMvc.perform(put(END_POINT_PATH + "/" + userId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(testUserDTO)))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void testUpdateShouldReturn400BadRequest() throws Exception {
        long userId = 999L;

        mockMvc.perform(put(END_POINT_PATH + "/" + userId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(UserDTO.builder().build())))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasSize(4)))
                .andExpect(jsonPath("$.errors", hasItem("Please enter email.")))
                .andExpect(jsonPath("$.errors", hasItem("Please enter first name.")))
                .andExpect(jsonPath("$.errors", hasItem("Please enter last name.")))
                .andExpect(jsonPath("$.errors", hasItem("Please enter birth date.")))
                .andDo(print());
    }

    @Test
    public void testUpdateShouldReturn202Accept() throws Exception {
        long userId = 999L;
        UserDTO testUserDTO = UserDTO.builder()
                .email(testEmail)
                .firstName(testFirstName)
                .lastName(testLastName)
                .birthDate(suitableBirthDate)
                .build();

        User expectedUser = UserDTO.toUser(testUserDTO);

        Mockito.when(userService.update(userId, testUserDTO)).thenReturn(expectedUser);

        mockMvc.perform(put(END_POINT_PATH + "/" + userId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(testUserDTO)))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.email", is(expectedUser.getEmail())))
                .andExpect(jsonPath("$.firstName", is(expectedUser.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(expectedUser.getLastName())))
                .andDo(print());
    }

    @Test
    public void testPatchShouldReturn404NotFound() throws Exception {
        long userId = 999L;

        HashMap<String, String> patchDTO = new HashMap<>();
        patchDTO.put("email", "patched@gmail.com");
        patchDTO.put("firstName", "Patched first name");

        Mockito.when(userService.patch(userId, patchDTO)).thenThrow(UserNotFoundException.class);

        mockMvc.perform(patch(END_POINT_PATH + "/" + userId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(patchDTO)))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void testPatchShouldReturn202Accept() throws Exception {
        long userId = 999L;

        HashMap<String, String> patchDTO = new HashMap<>();
        patchDTO.put("email", "patched@gmail.com");
        patchDTO.put("firstName", "Patched first name");

        User expectedUser = User.builder()
                .email("patched@gmail.com")
                .firstName("Patched first name")
                .build();

        Mockito.when(userService.patch(userId, patchDTO)).thenReturn(expectedUser);

        mockMvc.perform(patch(END_POINT_PATH + "/" + userId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(patchDTO)))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.email", is(expectedUser.getEmail())))
                .andExpect(jsonPath("$.firstName", is(expectedUser.getFirstName())))
                .andDo(print());
    }

    @Test
    public void testDeleteShouldReturn204NoContent() throws Exception {
        Mockito.doNothing().when(userService).deleteById(anyLong());

        mockMvc.perform(delete(END_POINT_PATH + "/" + anyLong()))
                .andExpect(status().isNoContent())
                .andDo(print());
    }
}
