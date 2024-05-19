package com.example.restaurant_universitaire.Controller;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.restaurant_universitaire.Auth.ExceptionModel;
import com.example.restaurant_universitaire.Auth.ResponseModel;
import com.example.restaurant_universitaire.Auth.ResponseObjModel;
import com.example.restaurant_universitaire.Model.user;
import com.example.restaurant_universitaire.Repository.userRepository;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/auth")
public class SecurityController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final userRepository userRepository;

    @Value("${app.auth.token.key}")
    private String tokenKey;

    @Autowired
    public SecurityController(AuthenticationManager authenticationManager, UserDetailsService userDetailsService,
            userRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<Object> authenticate(
            @RequestBody user login,
            HttpServletRequest request,
            @RequestHeader(value = "User-Agent", required = false) String userAgent) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(login.getMail(), login.getMot_de_passe()));
        } catch (BadCredentialsException e) {
            var res = new ResponseModel();
            res.setMessage("Invalid Credentials")
                    .setStatus(HttpStatus.UNAUTHORIZED)
                    .setCode(HttpStatus.UNAUTHORIZED.value());
            return new ResponseEntity<>(res, res.getStatus());
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(login.getMail());
        var date = new Date(System.currentTimeMillis());
        var token = JWT.create()
                .withSubject(userDetails.getUsername())
                .withIssuedAt(date)
                .withExpiresAt(new Date(System.currentTimeMillis() + (5 * 60 * 1000)))
                .withIssuer("fx-repo@" + request.getRemoteHost())
                .withClaim("roles", userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .sign(Algorithm.HMAC256(tokenKey));

        HashMap<String, String> resHash = new HashMap<>(2);
        resHash.put("token", token);

        var res = new ResponseObjModel();
        res.setData(resHash)
                .setCode(HttpStatus.OK.value())
                .setStatus(HttpStatus.OK)
                .setMessage("Authenticated Successfully");
        return new ResponseEntity<>(res, res.getStatus());
    }

    @PostMapping("/createAccount")git branch -M main 
    public ResponseEntity<Object> createAccount(@RequestBody ResponseModel accountData) {
        if (accountData.getPassword().length() > 20 || accountData.getPassword().length() < 6) {
            var res = new ExceptionModel();
            res.setTimestamp(Timestamp.from(Instant.now()))
                    .setMessage("Password must be between 6 and 20 characters")
                    .setStatus(HttpStatus.BAD_REQUEST)
                    .setCode(HttpStatus.BAD_REQUEST.value());
            return new ResponseEntity<>(res, res.getStatus());
        }

        user newUser = new user();
        newUser.setMail(accountData.getUsername());
        newUser.setMot_de_passe(passwordEncoder.encode(accountData.getPassword()));

        var savedUser = userRepository.save(newUser);
        HashMap<String, Object> resHash = new HashMap<>(4);
        resHash.put("id", savedUser.getId());
        resHash.put("email", savedUser.getMail());
        resHash.put("username", savedUser.getNom());
        resHash.put("roles", savedUser.getRole());

        var res = new ResponseObjModel();
        res.setData(resHash)
                .setCode(HttpStatus.CREATED.value())
                .setStatus(HttpStatus.CREATED)
                .setMessage("Account Created");
        return new ResponseEntity<>(res, res.getStatus());
    }
}
