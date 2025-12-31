package com.radiuk.user_management_service.mapper;

import com.radiuk.user_management_service.dto.UserRegistrationDto;
import com.radiuk.user_management_service.dto.UserResponseDto;
import com.radiuk.user_management_service.dto.UserUpdateDto;
import com.radiuk.user_management_service.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    UserResponseDto toUserResponseDto(User user);

    User fromRegistrationDto(UserRegistrationDto userRegistrationDto);

    void updateFromDto(UserUpdateDto dto, @MappingTarget User entity);
}
