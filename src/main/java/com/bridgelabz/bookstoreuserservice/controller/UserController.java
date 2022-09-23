package com.bridgelabz.bookstoreuserservice.controller;

import com.bridgelabz.bookstoreuserservice.dto.UserDto;
import com.bridgelabz.bookstoreuserservice.model.UserModel;
import com.bridgelabz.bookstoreuserservice.service.IUserService;
import com.bridgelabz.bookstoreuserservice.util.Response;
import com.bridgelabz.bookstoreuserservice.util.ResponseToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    IUserService userService;

    @PostMapping("/createuser")
    public ResponseEntity<Response> createUser(@RequestBody UserDto userDto) {
        Response response = userService.createUser(userDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Response> updateUser(@RequestHeader String token,
                                               @RequestBody UserDto userDto,
                                               @PathVariable Long userId) {
        Response response = userService.updateUser(userId, token, userDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/getuserdata")
    public ResponseEntity<List<?>> getUserData(@RequestParam String token) {
        List<UserModel> response = userService.getUserData(token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/deleteuser")
    public ResponseEntity<Response> deleteUser(@PathVariable Long userId,
                                               @RequestHeader String token) {
        Response response = userService.deleteUser(userId, token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseToken> login(@RequestParam String email,
                                               @RequestParam String password) {
        ResponseToken responseToken = userService.login(email, password);
        return new ResponseEntity<>(responseToken, HttpStatus.OK);
    }

    @GetMapping("/verify")
    public ResponseEntity<Response> verify(@RequestParam String token) {
        Response response = userService.verify(token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/resetpassword")
    public ResponseEntity<Response> resetPassword(@RequestBody String resetPassword,
                                                  @PathVariable String token) {
        Response response = userService.resetPassword(resetPassword, token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/forgotpassword")
    public ResponseEntity<Response> forgotPassword(@RequestParam String emailId) {
        Response response = userService.forgotPassword(emailId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/sendotp")
    public ResponseEntity<Response> sendOtp(@RequestHeader String token) {
        Response response = userService.sendOtp(token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/verifyotp/{otp}")
    public ResponseEntity<Response> validateOtp(@PathVariable Long otp, @RequestHeader String token) {
        Response response = userService.validateOtp(otp, token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/purchasesubscription")
    public ResponseEntity<Response> purchaseSubscription(@RequestHeader String token) {
        Response response = userService.purchaseSubscription(token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/validate/{token}")
    public Boolean validate(@PathVariable String token) {
        return userService.validate(token);
    }
}
