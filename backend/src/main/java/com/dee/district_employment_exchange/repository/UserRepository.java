package com.dee.district_employment_exchange.repository;

import com.dee.district_employment_exchange.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    // Count by role
    long countByRole(User.Role role);

    // Find by role
    List<User> findByRole(User.Role role);

    // Find active/inactive users
    List<User> findByIsActive(Boolean isActive);
}