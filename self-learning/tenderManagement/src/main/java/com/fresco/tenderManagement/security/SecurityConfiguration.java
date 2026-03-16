package com.fresco.tenderManagement.security;

import com.fresco.tenderManagement.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration   // marks this as a configuration class (Spring reads it at startup)
@EnableWebSecurity  // turns on Spring Security
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private LoginService loginService;

    @Autowired
    private AuthenticationFilter authFilter;

    // tells Spring Security to use LoginService for loading users during authentication
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(loginService);
    }

    // these paths bypass security entirely — no filter runs on them
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/h2-console/**")
                .antMatchers("/login")
                .antMatchers("/v3/api-docs/**")  // swagger
                .antMatchers("/swagger-ui/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()   // disable CSRF (not needed for stateless REST APIs)
                .cors().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // no sessions — JWT is stateless
                .and()
                .authorizeRequests()
                // BIDDER endpoints
                .antMatchers("/bidding/add").hasAuthority("BIDDER")
                // APPROVER only endpoint
                .antMatchers("/bidding/update/**").hasAuthority("APPROVER")
                // both BIDDER and APPROVER can access list and delete
                .antMatchers("/bidding/list").hasAnyAuthority("BIDDER", "APPROVER")
                .antMatchers("/bidding/delete/**").hasAnyAuthority("BIDDER", "APPROVER")
                .anyRequest().authenticated()
                .and()
                // return 401 when no JWT is present (instead of redirect to login page)
                .exceptionHandling()
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                .and()
                // run our JWT filter BEFORE Spring's default username/password filter
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // NoOpPasswordEncoder = passwords stored as plain text (matches the test data)
        return NoOpPasswordEncoder.getInstance();
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }
}