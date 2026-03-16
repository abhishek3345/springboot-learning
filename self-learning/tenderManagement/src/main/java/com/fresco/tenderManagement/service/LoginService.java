package com.fresco.tenderManagement.service;

import com.fresco.tenderManagement.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LoginService implements UserDetailsService {

    @Autowired
    private UserService userService;

    // Spring Security calls this during authentication.
    // The "username" parameter here is the email (because we pass email to authenticate())
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserModel user = userService.getUserByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
        // build a Spring Security UserDetails object from our UserModel
        List<GrantedAuthority> authorities = buildUserAuthority(user.getRole().getRolename());
        return buildUserForAuthentication(user, authorities);
    }

    // builds the Spring Security User object (from org.springframework.security.core.userdetails)
    // email is used as the "username" here so authenticate() works with email
    private UserDetails buildUserForAuthentication(UserModel user, List<GrantedAuthority> authorities) {
        return new User(user.getEmail(), user.getPassword(), authorities);
    }

    // wraps the role string into a GrantedAuthority list
    // SimpleGrantedAuthority("BIDDER") means this user has the BIDDER authority
    private List<GrantedAuthority> buildUserAuthority(String userRole) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(userRole));
        return authorities;
    }
}