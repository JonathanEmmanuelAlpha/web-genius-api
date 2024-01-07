package com.webgenius.webgeniusapi.user;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, ObjectId> {
    User findByEmail(String email);

    List<User> findByRolesIn(Role role);
}
