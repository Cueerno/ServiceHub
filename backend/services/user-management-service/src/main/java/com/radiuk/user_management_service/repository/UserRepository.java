package com.radiuk.user_management_service.repository;

import com.radiuk.user_management_service.entity.User;
import com.radiuk.user_management_service.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Page<User> findByFirstnameContainingIgnoreCase(Pageable pageable, String firstname);

    List<User> findAllByRole(Role role);

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);
}
