package com.spring.demo.db;

import com.spring.demo.entities.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {

    User findDistinctFirstByUsernameIgnoreCase(String username);
}
