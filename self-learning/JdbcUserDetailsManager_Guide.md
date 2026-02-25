# JdbcUserDetailsManager Configuration & Implementation

## What is JdbcUserDetailsManager?

Instead of creating `UserAuthService` that queries your custom User entity, **JdbcUserDetailsManager** directly loads user details from the database using predefined SQL queries.

Spring Security expects specific table structures for JDBC authentication.

---

## Your Database Tables

```
USER Table:
╔════════╦══════════╦══════════╗
║ userid ║ username ║ password ║
╠════════╬══════════╬══════════╣
║   1    ║  jack    ║ pass_word║
║   2    ║  bob     ║ pass_word║
║   3    ║  apple   ║ pass_word║
║   4    ║  glaxo   ║ pass_word║
╚════════╩══════════╩══════════╝

USER_ROLE Table (Many-to-Many):
╔════════╦═══════════╗
║ userid ║ role      ║
╠════════╬═══════════╣
║   1    ║ CONSUMER  ║
║   2    ║ CONSUMER  ║
║   3    ║ SELLER    ║
║   4    ║ SELLER    ║
╚════════╩═══════════╝
```

---

## Configuration in ApiSecurityConfig

```java
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApiSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;  // Database connection

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private ApiAuthenticationEntryPoint entryPoint;

    // ========== CONFIGURE AUTHENTICATION WITH JDBC ==========
    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
            .dataSource(dataSource)
            // Query to load user by username
            .usersByUsernameQuery(
                "SELECT username, password, true as enabled " +
                "FROM user " +
                "WHERE username = ?"
            )
            // Query to load roles/authorities for user
            .authoritiesByUsernameQuery(
                "SELECT u.username, ur.role " +
                "FROM user u " +
                "JOIN user_role ur ON u.userid = ur.userid " +
                "WHERE u.username = ?"
            )
            .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .cors().disable()
            .authorizeRequests()
            .antMatchers("/api/public/**").permitAll()
            .antMatchers("/api/auth/consumer/**").hasAuthority("CONSUMER")
            .antMatchers("/api/auth/seller/**").hasAuthority("SELLER")
            .anyRequest().authenticated()
            .and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .exceptionHandling().authenticationEntryPoint(entryPoint)
            .and()
            .addFilterBefore(jwtAuthenticationFilter, 
                UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}
```

---

## How JdbcUserDetailsManager Works

### When User Logs In (POST /api/public/login):

```
Client Request:
{
  "username": "bob",
  "password": "pass_word"
}
    ↓
AuthenticationManager calls:
    ↓
JdbcUserDetailsManager executes:
    ↓
Query 1: usersByUsernameQuery
    ↓
    "SELECT username, password, true as enabled 
     FROM user 
     WHERE username = 'bob'"
    ↓
    Result: (bob, pass_word, true)
    ↓
Query 2: authoritiesByUsernameQuery
    ↓
    "SELECT u.username, ur.role 
     FROM user u 
     JOIN user_role ur ON u.userid = ur.userid 
     WHERE u.username = 'bob'"
    ↓
    Result: (bob, CONSUMER)
    ↓
UserDetails object created:
    ├─ username: "bob"
    ├─ password: "pass_word"
    ├─ authorities: [CONSUMER]
    └─ enabled: true
    ↓
PasswordEncoder verifies:
    request.password == user.password
    "pass_word" == "pass_word" ✓
    ↓
Generate JWT Token:
    return ResponseEntity.ok(jwtUtil.generateToken(user));
```

---

## Login Endpoint (No Change Needed)

```java
@RestController
@RequestMapping("/api/public")
public class PublicController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User requestBody) {
        // AuthenticationManager uses JdbcUserDetailsManager internally
        // to load user and verify password
        
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                requestBody.getUsername(),
                requestBody.getPassword()
            )
        );
        
        // After successful authentication, create temporary user object
        // to generate JWT token
        UserDetails userDetails = loadUserDetails(requestBody.getUsername());
        
        return ResponseEntity.ok(jwtUtil.generateToken(userDetails));
    }
    
    // Helper method to load user for JWT generation
    private UserDetails loadUserDetails(String username) {
        // You need to query database again to get full user for JWT
        // OR use JdbcUserDetailsManager directly
        
        // Option 1: Create JdbcUserDetailsManager bean and use it
        // (See below)
        
        // Option 2: Use custom query
        // SELECT username, password, true FROM user WHERE username=?
        // plus get roles from user_role join
    }
}
```

---

## Better Approach: Create JdbcUserDetailsManager Bean

```java
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApiSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private ApiAuthenticationEntryPoint entryPoint;

    // ========== CREATE JDBC USER DETAILS MANAGER BEAN ==========
    @Bean
    public JdbcUserDetailsManager jdbcUserDetailsManager() {
        JdbcUserDetailsManager manager = new JdbcUserDetailsManager(dataSource);
        
        // Set the query that loads user by username
        manager.setUsersByUsernameQuery(
            "SELECT username, password, true as enabled " +
            "FROM user " +
            "WHERE username = ?"
        );
        
        // Set the query that loads authorities/roles for user
        manager.setAuthoritiesByUsernameQuery(
            "SELECT u.username, ur.role " +
            "FROM user u " +
            "JOIN user_role ur ON u.userid = ur.userid " +
            "WHERE u.username = ?"
        );
        
        return manager;
    }

    // ========== CONFIGURE AUTHENTICATION ==========
    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(jdbcUserDetailsManager())
            .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .cors().disable()
            .authorizeRequests()
            .antMatchers("/api/public/**").permitAll()
            .antMatchers("/api/auth/consumer/**").hasAuthority("CONSUMER")
            .antMatchers("/api/auth/seller/**").hasAuthority("SELLER")
            .anyRequest().authenticated()
            .and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .exceptionHandling().authenticationEntryPoint(entryPoint)
            .and()
            .addFilterBefore(jwtAuthenticationFilter, 
                UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}
```

---

## Updated Login Controller with JdbcUserDetailsManager

```java
@RestController
@RequestMapping("/api/public")
public class PublicController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private JdbcUserDetailsManager jdbcUserDetailsManager;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User requestBody) {
        
        // Authenticate (verify password)
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                requestBody.getUsername(),
                requestBody.getPassword()
            )
        );
        
        // Load user details (including authorities/roles)
        UserDetails userDetails = jdbcUserDetailsManager
            .loadUserByUsername(requestBody.getUsername());
        
        // Generate JWT token with username and authorities
        String token = jwtUtil.generateToken(userDetails);
        
        return ResponseEntity.ok(token);
    }
}
```

---

## JwtUtil Updated to Work with UserDetails

```java
@Component
public class JwtUtil {
    
    @Value("${jwt.secret}")
    private String secretKey;
    
    @Value("${jwt.token.validity}")
    private Integer tokenValidity;

    // ========== GENERATE TOKEN FROM USERDETAILS ==========
    public String generateToken(UserDetails userDetails) {
        // Extract authorities/roles
        String roles = userDetails.getAuthorities().stream()
            .map(auth -> auth.getAuthority())
            .collect(Collectors.joining(","));
        
        return Jwts.builder()
            .setSubject(userDetails.getUsername())
            .claim("roles", roles)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + tokenValidity))
            .signWith(SignatureAlgorithm.HS512, secretKey)
            .compact();
    }

    // ========== VALIDATE TOKEN ==========
    public boolean validateToken(final String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    // ========== GET CLAIMS FROM TOKEN ==========
    public Claims parseClaims(String token) {
        return Jwts.parser()
            .setSigningKey(secretKey)
            .parseClaimsJws(token)
            .getBody();
    }
}
```

---

## JwtAuthenticationFilter - Updated

```java
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private JdbcUserDetailsManager jdbcUserDetailsManager;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        if (!hasAuthorizationBearer(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = request.getHeader("JWT");

        if (!jwtUtil.validateToken(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        setAuthenticationContext(token, request);
        filterChain.doFilter(request, response);
    }

    private boolean hasAuthorizationBearer(HttpServletRequest request) {
        String header = request.getHeader("JWT");
        return header != null && !header.isEmpty();
    }

    private void setAuthenticationContext(String token, HttpServletRequest request) {
        Claims claims = jwtUtil.parseClaims(token);
        String username = claims.getSubject();
        
        // Load user details from database using JdbcUserDetailsManager
        UserDetails userDetails = jdbcUserDetailsManager
            .loadUserByUsername(username);
        
        UsernamePasswordAuthenticationToken authentication = 
            new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
            );
        
        authentication.setDetails(
            new WebAuthenticationDetailsSource().buildDetails(request));
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
```

---

## Database Schema for JdbcUserDetailsManager

Your existing tables are perfect. Just make sure column names match:

```sql
-- USER table (for usersByUsernameQuery)
CREATE TABLE user (
    userid INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    enabled BOOLEAN DEFAULT true
);

-- USER_ROLE table (for authoritiesByUsernameQuery)
CREATE TABLE user_role (
    userid INT NOT NULL,
    role VARCHAR(50) NOT NULL,
    FOREIGN KEY (userid) REFERENCES user(userid)
);

-- Insert your data
INSERT INTO user (userid, username, password) VALUES
(1, 'jack', 'pass_word'),
(2, 'bob', 'pass_word'),
(3, 'apple', 'pass_word'),
(4, 'glaxo', 'pass_word');

INSERT INTO user_role (userid, role) VALUES
(1, 'CONSUMER'),
(2, 'CONSUMER'),
(3, 'SELLER'),
(4, 'SELLER');
```

---

## Key Points

1. **JdbcUserDetailsManager** loads users directly from database
2. **usersByUsernameQuery** gets username and password
3. **authoritiesByUsernameQuery** gets roles/authorities
4. Both queries must return specific columns (username, password/role)
5. JwtAuthenticationFilter uses JdbcUserDetailsManager to reload user for JWT generation
6. No need for custom User entity or UserAuthService

---

## Flow Summary

```
Login Request
    ↓
AuthenticationManager uses JdbcUserDetailsManager
    ↓
usersByUsernameQuery: SELECT username, password FROM user WHERE username=?
    ↓
authoritiesByUsernameQuery: SELECT username, role FROM user_role WHERE ...
    ↓
Verify password matches
    ↓
Load UserDetails from database
    ↓
Generate JWT with username + roles
    ↓
Return token
    ↓
On protected request with token:
    ↓
JwtAuthenticationFilter validates token
    ↓
Reload UserDetails from database
    ↓
Set in SecurityContext with authorities
    ↓
Spring Security checks role
    ↓
Execute controller or return 403
```

That's it! No custom User entity, no UserAuthService - just JdbcUserDetailsManager handling everything.
