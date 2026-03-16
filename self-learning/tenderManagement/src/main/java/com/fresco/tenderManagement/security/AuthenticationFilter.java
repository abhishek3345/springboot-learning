package com.fresco.tenderManagement.security;

import com.fresco.tenderManagement.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component  // makes Spring manage this as a bean so it can be injected into SecurityConfiguration
public class AuthenticationFilter extends OncePerRequestFilter {
    // OncePerRequestFilter = this runs exactly once per HTTP request

    @Autowired
    private JWTUtil jWTUtil;

    @Autowired
    private LoginService loginService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // Step 1: Read the Authorization header → "Bearer eyJhbGci..."
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String email = null;

        // Step 2: Check it starts with "Bearer " and extract the token part
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7); // everything after "Bearer "
            email = jWTUtil.getUsernameFromToken(token); // extract email from token
        }

        // Step 3: If we got an email AND nobody is already authenticated in this request
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // load the user from DB using the email
            UserDetails userDetails = loginService.loadUserByUsername(email);

            // Step 4: Validate token (email matches + not expired)
            if (jWTUtil.validateToken(token, userDetails)) {
                // Step 5: Create an authentication token and put it in the SecurityContext
                // This is what makes Spring know "this request is authenticated as this user"
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Step 6: Always pass the request along the filter chain
        filterChain.doFilter(request, response);
    }
}