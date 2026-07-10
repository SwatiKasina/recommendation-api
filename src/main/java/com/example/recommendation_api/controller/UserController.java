package com.example.recommendation_api.controller;


import com.example.recommendation_api.model.LoginModel;
import com.example.recommendation_api.model.User;
import com.example.recommendation_api.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("api/v1/user")
@CrossOrigin("*")
@Tag(name = "User Controller", description = "APIs for user signup, login, and retrieval")

public class UserController {

    private final UserRepository userRepository;

     public UserController( UserRepository userRepository){
         this.userRepository = userRepository;
     }

    @Operation(summary = "Signup new user")
     @PostMapping(
             value = "/signup",
             consumes = MediaType.APPLICATION_JSON_VALUE,
             produces = MediaType.APPLICATION_JSON_VALUE
     )
    public String signupUser (@RequestBody User userDto){
        userRepository.save(userDto);
        return "user saved successfully";

     }
    @Operation(summary = "Login user")
     @PostMapping(
             value= "/login",
             consumes = MediaType.APPLICATION_JSON_VALUE,
             produces = MediaType.APPLICATION_JSON_VALUE

     )
    public User loginUser (@RequestBody LoginModel loginModel){
        if (loginModel.getEmail() == null || loginModel.getPassword() == null) {
            return null;
        }

        return userRepository
                .findByEmailAndPassword(
                        loginModel.getEmail().trim(),
                        loginModel.getPassword().trim()
                )
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
    @Operation(summary = "Get user by ID")
     @GetMapping(
             value="/{id}",
             produces = MediaType.APPLICATION_JSON_VALUE
     )
     public Optional<User> getUser(@PathVariable String id){

         return userRepository.findById(id);
     }


}
