package com.radiuk.user_management_service.service;

import com.radiuk.user_management_service.dto.UserResponseDto;
import com.radiuk.user_management_service.dto.UserUpdateDto;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;

public interface UserService {

    List<UserResponseDto> getUsersBy(int page, int limit, String filterByName, String sortBy, String orderBy, Jwt jwt);

    List<UserResponseDto> getUsersByIds(List<Long> ids);

    UserResponseDto getUserByToken(Jwt jwt);

    UserResponseDto getUserById(Jwt jwt, Long userId);

    UserResponseDto updateUserByToken(UserUpdateDto userUpdateDto, Jwt jwt);

    UserResponseDto updateUserById(UserUpdateDto userUpdateDto, Jwt jwt, Long userId);

    void deleteUserByToken(Jwt jwt);

    void deleteUserById(Jwt jwt, Long userId);
}
