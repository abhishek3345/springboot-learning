package com.learning.demo.service;

import com.learning.demo.model.User;
import com.learning.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserAuthService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {


//       return userRepository.findByUsername(username).orElseThrow(()
//               -> new UsernameNotFoundException("User not found"));
        Optional<User>userOptional = userRepository.findByUsername(username);
        if(!userOptional.isPresent()){
            throw new UsernameNotFoundException("User not found");
        }
        User user = userOptional.get();
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(user.getAuthorities())
                .build();
    }


}
