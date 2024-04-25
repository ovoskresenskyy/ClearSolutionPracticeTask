package com.example.clearsolutionstesttask.mapper;

import com.example.clearsolutionstesttask.dto.UserDto;
import com.example.clearsolutionstesttask.entity.User;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * Mapper interface for mapping between {@link UserDto} and {@link User} entities.
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

  @Mapping(target = "id", ignore = true)
  User toEntity(UserDto dto);

  UserDto toDto(User entity);

  List<UserDto> toDto(List<User> entity);

  @Mapping(target = "id", ignore = true)
  void updateEntity(UserDto dto, @MappingTarget User entity);
}
