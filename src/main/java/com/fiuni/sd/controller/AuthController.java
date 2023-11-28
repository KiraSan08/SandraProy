package com.fiuni.sd.controller;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.validation.Valid;

import com.fiuni.sd.dao.IRoleDao;
import com.fiuni.sd.dao.IUserDao;
import com.fiuni.sd.domain.User;
import com.fiuni.sd.domain.Role;
import com.fiuni.sd.dto.auth.UserRegistrationDto;

import com.fiuni.sd.security.AuthRequest;
import com.fiuni.sd.security.AuthResponse;
import com.fiuni.sd.security.jwt.JwtTokenUtil;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
public class AuthController {

    @Autowired
    AuthenticationManager authManager;

    @Autowired
    JwtTokenUtil jwtUtil;

    @Autowired
    private IUserDao userDao;

    @Autowired
    private IRoleDao roleDao;

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthRequest request) {
        try {
            final Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

            final User user = (User) authentication.getPrincipal();
            final String accessToken = jwtUtil.generateAccessToken(user);
            final AuthResponse response = new AuthResponse(user.getEmail(), accessToken);
            return ResponseEntity.ok().body(response);
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/auth/register")
    public ResponseEntity<?> register(@RequestBody @Valid UserRegistrationDto request) {
        try {
            if (userDao.findByEmail(request.getEmail()).isPresent()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email already in use");
            }

            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String password = passwordEncoder.encode(request.getPassword());

            User newUser = new User();
            newUser.setName(request.getFirstName());
            newUser.setLastName(request.getLastName());
            newUser.setEmail(request.getEmail());
            newUser.setPassword(password);

            Set<Role> roles = new HashSet<>();
            for (String roleName : request.getRoles()) {
                Optional<Role> role = roleDao.findByName(roleName);
                role.ifPresent(roles::add);
            }

            newUser.setRoles(roles);
            userDao.save(newUser);

            return ResponseEntity.ok().body(newUser);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}