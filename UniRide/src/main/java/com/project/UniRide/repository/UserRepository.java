package com.project.uniride.repository;

import com.project.uniride.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);
    Optional<User> findByFirebaseUid(String firebaseUid);
    boolean existsByEmail(String email);

    @Query("{ 'accountType': 'DRIVER', 'isOnline': true, 'currentLocation': { $exists: true } }")
    List<User> findAvailableDrivers();
}
