package com.gkfcsolution.gkfcsolutionhotel.repository;

import com.gkfcsolution.gkfcsolutionhotel.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Created on 2025 at 19:15
 * File: null.java
 * Project: springboot-movies-bookinks
 *
 * @author Frank GUEKENG
 * @date 13/12/2025
 * @time 19:15
 */
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
