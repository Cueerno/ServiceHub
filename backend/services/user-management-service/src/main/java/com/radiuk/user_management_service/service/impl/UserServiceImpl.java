package com.radiuk.user_management_service.service.impl;

import com.radiuk.user_management_service.dto.UserResponseDto;
import com.radiuk.user_management_service.dto.UserUpdateDto;
import com.radiuk.user_management_service.entity.User;
import com.radiuk.user_management_service.exception.UserNotFoundException;
import com.radiuk.user_management_service.exception.UserNotUpdatedException;
import com.radiuk.user_management_service.mapper.UserMapper;
import com.radiuk.user_management_service.repository.UserRepository;
import com.radiuk.user_management_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;

    @Override
    public List<UserResponseDto> getUsersBy(int page, int limit, String filterByName, String sortBy, String orderBy, Jwt jwt) {
        Sort.Direction direction = orderBy.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(direction, sortBy));

        List<User> users;

        if (filterByName == null || filterByName.isBlank()) {
            users = userRepository.findAll(pageable).getContent();
        }  else {
            users = userRepository.findByFirstnameContainingIgnoreCase(pageable, filterByName).getContent();
        }

        return users.stream()
                .map(userMapper::toUserResponseDto).toList();
    }

    @Override
    public UserResponseDto getUserByToken(Jwt jwt) {
        return userMapper.toUserResponseDto(getUserByIdOrThrow(Long.valueOf(jwt.getSubject())));
    }

    @Override
    public UserResponseDto getUserById(Jwt jwt, Long userId) {
        return userMapper.toUserResponseDto(getUserByIdOrThrow(userId));
    }

    @Override
    public UserResponseDto updateUserByToken(UserUpdateDto dto, Jwt jwt) {
        User user = getUserByIdOrThrow(Long.valueOf(jwt.getSubject()));
        return userMapper.toUserResponseDto(updateUser(dto, user));
    }

    @Override
    public UserResponseDto updateUserById(UserUpdateDto dto, Jwt jwt, Long userId) {
        User user = getUserByIdOrThrow(userId);
        return userMapper.toUserResponseDto(updateUser(dto, user));
    }

    @Override
    public void deleteUserByToken(Jwt jwt) {
        userRepository.deleteById(getUserByIdOrThrow(Long.valueOf(jwt.getSubject())).getId());
    }

    @Override
    public void deleteUserById(Jwt jwt, Long userId) {
        userRepository.deleteById(getUserByIdOrThrow(userId).getId());
    }

    public User getUserByIdOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(String.format("User with id %d not found", userId)));
    }

    private User updateUser(UserUpdateDto dto, User user) {
        if (dto.email() != null && !dto.email().equals(user.getEmail()) && userRepository.existsByEmail(dto.email())) {
            throw new UserNotUpdatedException("User with this email already exists");
        }

        if (dto.username() != null && !dto.username().equals(user.getUsername()) && userRepository.existsByUsername(dto.username())) {
            throw new UserNotUpdatedException("User with this username already exists");
        }

        userMapper.updateFromDto(dto, user);

        return user;
    }
}
