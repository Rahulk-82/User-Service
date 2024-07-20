package com.auth.user.controllers;

import com.auth.user.dtos.LoginDto;
import com.auth.user.dtos.SignupRequestDto;
import com.auth.user.models.Token;
import com.auth.user.models.User;
import com.auth.user.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    private UserService userservice;

    @PostMapping("/signup")
    public User signUp(@RequestBody SignupRequestDto signupRequestDto){
        String email=signupRequestDto.getEmail();
        String password=signupRequestDto.getPassword();
        String name=signupRequestDto.getName();

        return userservice.signUp(name,email,password);
    }

    @PostMapping("/login")
    public Token login(@RequestBody LoginDto loginDto){
        String email=loginDto.getEmail();
        String password=loginDto.getPassword();
        return userservice.login(email,password);

    }

    // got log out token should be deleted for user.

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestParam ("token") String token){
        userservice.logout(token);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/validate/{token}")
    public boolean validateToken(@PathVariable("token") String token){

        return userservice.validateToken(token);
    }

}
