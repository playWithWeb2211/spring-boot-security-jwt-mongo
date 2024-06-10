package org.spring.jwt.mongo.project.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spring.jwt.mongo.project.models.ERole;
import org.spring.jwt.mongo.project.models.Role;
import org.spring.jwt.mongo.project.models.User;
import org.spring.jwt.mongo.project.payload.request.LoginRequest;
import org.spring.jwt.mongo.project.payload.request.SignupRequest;
import org.spring.jwt.mongo.project.payload.response.JwtResponse;
import org.spring.jwt.mongo.project.payload.response.MessageResponse;
import org.spring.jwt.mongo.project.repository.RoleRepository;
import org.spring.jwt.mongo.project.repository.UserRepository;
import org.spring.jwt.mongo.project.security.jwt.JwtUtils;
import org.spring.jwt.mongo.project.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*",maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    JwtUtils jwtUtils;

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @GetMapping("/hello")
    public ResponseEntity<String> getHello(){
        return ResponseEntity
                .ok("Hello Guest How May I help you!!");
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest){
        logger.debug("\n\n\n");
        logger.debug("**************************************************************************************");
        logger.debug("Inside registerUser method");
        if(userRepository.existsByUsername(signupRequest.getUsername())){
            logger.error("Username is already taken");
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error : Username is already taken"));
        }

        if (userRepository.existsByEmail(signupRequest.getEmail())){
            logger.error("Email is already in use!");
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        logger.debug("Creating new User with Username : {} | email : {}",signupRequest.getUsername(),signupRequest.getEmail());
//      Create the new User
        User user = new User(signupRequest.getUsername(),
                signupRequest.getEmail(),
                encoder.encode(signupRequest.getPassword()));

        Set<String> strRoles = signupRequest.getRoles();

        Set<Role> roles = new HashSet<>();

        if(strRoles == null){
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error : Role is not found."));

            roles.add(userRole);
        }else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        logger.debug("Adding admin role : {}", adminRole.getName());
                        roles.add(adminRole);

                        break;
                    case "mod":
                        Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        logger.debug("Adding mod role : {}", modRole.getName());
                        roles.add(modRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        logger.debug("Adding user role : {}", userRole.getName());
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        logger.debug("Saving User Details ......");
        userRepository.save(user);
        logger.debug("Successfully Saved User Details ......");

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Validated @RequestBody LoginRequest loginRequest){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                                                 userDetails.getId(),
                                                 userDetails.getUsername(),
                                                 userDetails.getEmail(),
                                                 roles));


    }
}
