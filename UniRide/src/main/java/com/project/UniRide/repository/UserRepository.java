package com.project.uniride.repository;

import com.project.uniride.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);
    Optional<User> findByFirebaseUid(String firebaseUid);
    boolean existsByEmail(String email);
    List<User> findByAccountTypeAndIsOnlineTrue(User.AccountType accountType);
}
