package com.fresco.tenderManagement.repository;

import com.fresco.tenderManagement.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository  // tells Spring this is a DB access class
public interface UserRepository extends JpaRepository<UserModel, Integer> {
    // Spring auto-generates the SQL from the method name
    // findByEmail → SELECT * FROM users WHERE email = ?
    UserModel findByEmail(String email);
}