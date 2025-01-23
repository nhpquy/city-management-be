package com.project.citymanagement.repository;

import com.project.citymanagement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/** This interface represents the repository for users. */
public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByUsername(String username);
}
