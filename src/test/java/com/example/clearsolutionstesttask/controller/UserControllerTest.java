package com.example.clearsolutionstesttask.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.clearsolutionstesttask.dto.UserDto;
import com.example.clearsolutionstesttask.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserController.class)
public class UserControllerTest {

  private static final String END_POINT_PATH = "/users";

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private UserService userService;

  private UserDto testUserDto;

  @BeforeEach
  public void init() {
    testUserDto = UserDto.builder()
        .email("test@gmail.com")
        .firstName("Test first name")
        .lastName("Test last name")
        .birthDate(LocalDate.parse("1990-01-01"))
        .build();
  }

  @Test
  public void testCreateUser() throws Exception {
    when(userService.create(any())).thenReturn(testUserDto);

    String contentAsString = mockMvc.perform(
            post(END_POINT_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUserDto)))
        .andExpect(status().isOk())
        .andReturn()
        .getResponse()
        .getContentAsString();

    UserDto resultDto = objectMapper.readValue(contentAsString, UserDto.class);

    assertEquals(testUserDto, resultDto);
  }

  @Test
  public void testUpdateUser() throws Exception {
    long userId = 999L;
    when(userService.update(anyLong(), any())).thenReturn(testUserDto);

    String contentAsString = mockMvc.perform(
            put(END_POINT_PATH + "/" + userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUserDto)))
        .andExpect(status().isOk())
        .andReturn()
        .getResponse()
        .getContentAsString();

    UserDto resultDto = objectMapper.readValue(contentAsString, UserDto.class);

    assertEquals(testUserDto, resultDto);
  }

  @Test
  public void testDeleteUser() throws Exception {
    doNothing().when(userService).deleteById(anyLong());

    mockMvc.perform(delete(END_POINT_PATH + "/" + anyLong()))
        .andExpect(status().isOk());
  }

  @Test
  public void testFindAllByBirthDateRange() throws Exception {
    String fromDate = "2000-01-01";
    String toDate = "2020-01-01";

    List<UserDto> users = List.of(testUserDto);

    when(userService.findAllByBirthDateRange(LocalDate.parse(fromDate), LocalDate.parse(toDate)))
        .thenReturn(users);

    String contentAsString = mockMvc.perform(
            get(END_POINT_PATH + "/birth-date-range")
                .param("fromDate", fromDate)
                .param("toDate", toDate))
        .andExpect(status().isOk())
        .andReturn()
        .getResponse()
        .getContentAsString();

    List<UserDto> resultDto = List.of(objectMapper.readValue(contentAsString, UserDto[].class));

    assertEquals(users, resultDto);
  }
}
