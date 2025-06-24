package fr.ynov.testdevweb.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.ynov.testdevweb.entities.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}