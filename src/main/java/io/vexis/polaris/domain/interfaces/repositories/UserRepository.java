package io.vexis.polaris.domain.interfaces.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import io.vexis.polaris.domain.models.entities.User;

public interface UserRepository extends JpaRepository<User, UUID> {

    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);
    
}
