package com.radiuk.user_management_service.service.impl;

import com.radiuk.user_management_service.dto.UserResponseDto;
import com.radiuk.user_management_service.dto.UserUpdateDto;
import com.radiuk.user_management_service.entity.Role;
import com.radiuk.user_management_service.entity.User;
import com.radiuk.user_management_service.exception.UserNotFoundException;
import com.radiuk.user_management_service.exception.UserNotUpdatedException;
import com.radiuk.user_management_service.mapper.UserMapper;
import com.radiuk.user_management_service.repository.UserRepository;
import com.radiuk.user_management_service.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;

    @Override
    public List<UserResponseDto> getUsersBy(int page, int limit, String filterByName, String sortBy, String orderBy, Jwt jwt) {
        log.debug("Get users with page {} and limit {}, with filter by name {}, with sort by {} and order by {}", page, limit, filterByName, sortBy, orderBy);

        Pageable pageable = buildPageable(page, limit, sortBy, orderBy);

        List<User> users = (filterByName == null || filterByName.isBlank())
                ? userRepository.findAll(pageable).getContent()
                : userRepository.findByFirstnameContainingIgnoreCase(pageable, filterByName).getContent();

        log.info("Found {} users", users.size());
        return users.stream()
                .filter(u -> !isSelf(u, jwt))
                .filter(u -> canAccessUser(u, jwt))
                .map(userMapper::toUserResponseDto)
                .toList();
    }

    @Override
    public UserResponseDto getUserByToken(Jwt jwt) {
        log.debug("Get user with id {}", getUserIdFromToken(jwt));
        return userMapper.toUserResponseDto(getUserByIdOrThrow(getUserIdFromToken(jwt)));
    }

    @Override
    public UserResponseDto getUserById(Jwt jwt, Long userId) {
        log.info("Get user by id: targetUserId={}, requesterId={}", userId, jwt.getSubject());

        User user = getUserByIdOrThrow(userId);
        checkAccess(user, jwt);

        return userMapper.toUserResponseDto(user);
    }

    @Override
    public UserResponseDto updateUserByToken(UserUpdateDto dto, Jwt jwt) {
        log.info("Update user with id {}", getUserIdFromToken(jwt));
        User user = getUserByIdOrThrow(getUserIdFromToken(jwt));
        return userMapper.toUserResponseDto(updateUser(dto, user));
    }

    @Override
    public UserResponseDto updateUserById(UserUpdateDto dto, Jwt jwt, Long userId) {
        log.info(
                "Update user: targetUserId={}, requesterId={}, fields={}",
                userId,
                jwt.getSubject(),
                dto
        );

        User user = getUserByIdOrThrow(userId);
        checkAccess(user, jwt);

        log.info("User updated successfully: userId={}", userId);
        return userMapper.toUserResponseDto(updateUser(dto, user));
    }

    @Override
    public void deleteUserByToken(Jwt jwt) {
        log.info("Delete user with id {}", getUserIdFromToken(jwt));
        userRepository.deleteById(getUserIdFromToken(jwt));
    }

    public void deleteUserById(Jwt jwt, Long userId) {
        log.warn(
                "Delete user request: targetUserId={}, requesterId={}",
                userId,
                jwt.getSubject()
        );

        User user = getUserByIdOrThrow(userId);
        checkAccess(user, jwt);

        userRepository.deleteById(user.getId());

        log.warn("User deleted: userId={}", userId);
    }


    private void checkAccess(User user, Jwt jwt) {
        if (!canAccessUser(user, jwt)) {
            log.warn(
                    "Access denied: requesterId={}, targetUserId={}, roles={}",
                    jwt.getSubject(),
                    user.getId(),
                    getUserRolesFromToken(jwt)
            );
            throw new AccessDeniedException("Access denied");
        }
    }

    private boolean canAccessUser(User user, Jwt jwt) {
        if (isAdmin(jwt)) {
            log.debug("Admin access granted: requesterId={}", jwt.getSubject());
            return true;
        }

        if (isModerator(jwt)) {
            Long groupId = jwt.getClaim("groupId");
            boolean allowed = groupId != null
                    && user.getGroup() != null
                    && groupId.equals(user.getGroup().getId());

            log.debug(
                    "Moderator access check: requesterId={}, targetUserId={}, allowed={}",
                    jwt.getSubject(),
                    user.getId(),
                    allowed
            );

            return allowed;
        }

        log.debug(
                "Access denied by role: requesterId={}, roles={}",
                jwt.getSubject(),
                getUserRolesFromToken(jwt)
        );

        return false;
    }

    private boolean isAdmin(Jwt jwt) {
        return getUserRolesFromToken(jwt).contains(Role.ADMIN.name());
    }

    private boolean isModerator(Jwt jwt) {
        return getUserRolesFromToken(jwt).contains(Role.MODERATOR.name());
    }

    private List<String> getUserRolesFromToken(Jwt jwt) {
        return jwt.getClaim("authorities");
    }


    private boolean isSelf(User user, Jwt jwt) {
        return user.getId().equals(getUserIdFromToken(jwt));
    }

    private Long getUserIdFromToken(Jwt jwt) {
        return Long.valueOf(jwt.getSubject());
    }


    private Pageable buildPageable(int page, int limit, String sortBy, String orderBy) {
        Sort.Direction direction = orderBy.equalsIgnoreCase("desc")
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        return PageRequest.of(page - 1, limit, Sort.by(direction, sortBy));
    }

    private User getUserByIdOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(String.format("User with id %d not found", userId)));
    }

    private User updateUser(UserUpdateDto dto, User user) {

        if (dto.email() != null && !dto.email().equals(user.getEmail())
                && userRepository.existsByEmail(dto.email())) {

            log.warn(
                    "Failed to update user {}: email already exists",
                    user.getId()
            );
            throw new UserNotUpdatedException("User with this email already exists");
        }

        if (dto.username() != null && !dto.username().equals(user.getUsername())
                && userRepository.existsByUsername(dto.username())) {

            log.warn(
                    "Failed to update user {}: username already exists",
                    user.getId()
            );
            throw new UserNotUpdatedException("User with this username already exists");
        }

        log.debug("Applying updates to user {}", user.getId());
        userMapper.updateFromDto(dto, user);

        return user;
    }
}

