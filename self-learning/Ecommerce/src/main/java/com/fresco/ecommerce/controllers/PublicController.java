package com.fresco.ecommerce.controllers;

import com.fresco.ecommerce.config.JwtUtil;
import com.fresco.ecommerce.models.Product;
import com.fresco.ecommerce.models.User;
import com.fresco.ecommerce.repo.ProductRepo;
import com.fresco.ecommerce.service.UserAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public")
public class PublicController {

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private UserAuthService userAuthService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    // GET /api/public/product/search?keyword=tablet
    @GetMapping("/product/search")
    public ResponseEntity<List<Product>> getProducts(@RequestParam(required = false) String keyword) {

        if(keyword == null || keyword.trim().isEmpty()){
            return ResponseEntity.badRequest().build();
        }
        List<Product> product =  productRepo
                .findByProductNameContainingIgnoreCaseOrCategoryCategoryNameContainingIgnoreCase(
                        keyword, keyword);
        return ResponseEntity.ok(product);
    }

    // POST /api/public/login
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User requestBody) {
        try {

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestBody.getUsername(),
                            requestBody.getPassword()
                    )
            );
            User user = userAuthService.loadUserByUsername(requestBody.getUsername());
            return ResponseEntity.ok(jwtUtil.generateToken(user));
        }catch (BadCredentialsException | UsernameNotFoundException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }



    }
}