package com.fresco.tenderManagement.service;

import com.fresco.tenderManagement.model.UserModel;
import com.fresco.tenderManagement.repository.RoleRepository;
import com.fresco.tenderManagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service  // marks this as a Spring-managed service bean
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    // called by LoginService and BiddingService to get the full UserModel by email
    public UserModel getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}