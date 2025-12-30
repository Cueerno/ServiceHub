package com.radiuk.user_management_service.controller;

import com.radiuk.user_management_service.dto.UserResponseDto;
import com.radiuk.user_management_service.dto.UserUpdateDto;
import com.radiuk.user_management_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @GetMapping("")
    public ResponseEntity<List<UserResponseDto>> getUsersBy(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "30") int limit,
            @RequestParam(required = false) String filterByName,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "asc") String orderBy,
            @AuthenticationPrincipal Jwt jwt
    ) {
        return ResponseEntity.ok(userService.getUsersBy(page, limit, filterByName, sortBy, orderBy, jwt));
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getUserByToken(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(userService.getUserByToken(jwt));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(
            @PathVariable Long id,
            @AuthenticationPrincipal Jwt jwt
    ) {
        return ResponseEntity.ok(userService.getUserById(jwt, id));
    }

    @PatchMapping("/me")
    public ResponseEntity<UserResponseDto> updateUserByToken(@RequestBody UserUpdateDto userUpdateDto, @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateUserByToken(userUpdateDto, jwt));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUserById(
            @PathVariable Long id,
            @RequestBody UserUpdateDto userUpdateDto,
            @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateUserById(userUpdateDto, jwt, id));
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteUserByToken(@AuthenticationPrincipal Jwt jwt) {
        userService.deleteUserByToken(jwt);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(
            @PathVariable Long id,
            @AuthenticationPrincipal Jwt jwt
    ) {
        userService.deleteUserById(jwt, id);
        return ResponseEntity.noContent().build();
    }
}
