package com.techouts.service;

import com.techouts.model.User;
import com.techouts.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println(">>> LoadUserByUsername executing for email: " + email);

        // Make the query case-insensitive
        Optional<User> optionalUser = userService.findByEmail(email);

        if (optionalUser.isEmpty()) {
            System.out.println(">>> User not found for email: " + email);
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        User user = optionalUser.get();

        // Debug info
        System.out.println(">>> User found:");
        System.out.println("Email: " + user.getEmail());
        System.out.println("Name: " + user.getName());
        System.out.println("Password Hash: " + user.getPassword());
        //System.out.println("Role: " + user.getRole());

        // Build Spring Security UserDetails
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())  // Password is already BCrypt-encoded
                //.roles(user.getRole().name().replace("ROLE_", ""))  // CUSTOMER or ADMIN
                .build();
    }
}