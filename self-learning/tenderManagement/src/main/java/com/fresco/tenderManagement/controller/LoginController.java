package com.fresco.tenderManagement.controller;

import com.fresco.tenderManagement.dto.LoginDTO;
import com.fresco.tenderManagement.security.JWTUtil;
import com.fresco.tenderManagement.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController  // marks this as a REST controller — methods return JSON, not views
public class LoginController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private LoginService loginService;

    @Autowired
    private JWTUtil jwtTokenUtil;

    @PostMapping("/login")
    public Object authenticateUser(@RequestBody LoginDTO authenticationRequest) throws Exception {
        try {
            // Step 1: Try to authenticate with email + password
            // Internally Spring calls loginService.loadUserByUsername(email)
            // and checks the password matches
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationRequest.getEmail(),
                            authenticationRequest.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            // Wrong password or email not found → return 400
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid credentials");
        }

        // Step 2: Authentication succeeded — load user details and generate JWT
        final UserDetails userDetails = loginService.loadUserByUsername(
                authenticationRequest.getEmail()
        );
        final String jwt = jwtTokenUtil.generateToken(userDetails);

        // Step 3: Return {"jwt": "...", "status": 200} — the test expects both fields
        Map<String, Object> response = new HashMap<>();
        response.put("jwt", jwt);
        response.put("status", 200);
        return ResponseEntity.ok(response);
    }
}