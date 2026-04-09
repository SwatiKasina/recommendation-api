package com.example.recommendation_api.controller;
import com.example.recommendation_api.dao.UserDao;
import com.example.recommendation_api.dto.UserDto;
import com.example.recommendation_api.model.LoginModel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController

@RequestMapping("api/v1/user")
@CrossOrigin("*")

public class UserController {

    private final UserDao userDao;

     public UserController(UserDao userDao){
         this.userDao = userDao;

     }

     @PostMapping(
             value = "/signup",
             consumes = MediaType.APPLICATION_JSON_VALUE,
             produces = MediaType.APPLICATION_JSON_VALUE
     )
    public String signupUser (@RequestBody UserDto userDto){
        userDao.saveUser(userDto);
        return "user saved successfully";

     }

     @PostMapping(
             value= "/login",
             consumes = MediaType.APPLICATION_JSON_VALUE,
             produces = MediaType.APPLICATION_JSON_VALUE

     )
    public UserDto loginUser (@RequestBody LoginModel loginModel){
        return userDao.loginUser(loginModel.getEmail(),loginModel.getPassword());

     }

     @GetMapping(
             value="/user",
             produces = MediaType.APPLICATION_JSON_VALUE
     )
     public UserDto getUser(@RequestParam UUID id){

         return userDao.getUserById(id);
     }


}
