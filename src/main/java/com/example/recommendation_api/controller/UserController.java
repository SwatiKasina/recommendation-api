package com.example.recommendation_api.controller;
import com.example.recommendation_api.dao.UserDao;
import com.example.recommendation_api.dto.UserDto;
import com.example.recommendation_api.model.LoginModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController

@RequestMapping("api/v1/user")
@CrossOrigin("*")
@Tag(name = "User Controller", description = "APIs for user signup, login, and retrieval")

public class UserController {

    private final UserDao userDao;

     public UserController(UserDao userDao){
         this.userDao = userDao;

     }

    @Operation(summary = "Signup new user")
     @PostMapping(
             value = "/signup",
             consumes = MediaType.APPLICATION_JSON_VALUE,
             produces = MediaType.APPLICATION_JSON_VALUE
     )
    public String signupUser (@RequestBody UserDto userDto){
        userDao.saveUser(userDto);
        return "user saved successfully";

     }
    @Operation(summary = "Login user")
     @PostMapping(
             value= "/login",
             consumes = MediaType.APPLICATION_JSON_VALUE,
             produces = MediaType.APPLICATION_JSON_VALUE

     )
    public UserDto loginUser (@RequestBody LoginModel loginModel){
        return userDao.loginUser(loginModel);

     }
    @Operation(summary = "Get user by ID")
     @GetMapping(
             value="/{id}",
             produces = MediaType.APPLICATION_JSON_VALUE
     )
     public UserDto getUser(@PathVariable UUID id){

         return userDao.getUserById(id);
     }


}
