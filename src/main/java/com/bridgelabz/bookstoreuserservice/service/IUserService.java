package com.bridgelabz.bookstoreuserservice.service;

import com.bridgelabz.bookstoreuserservice.dto.UserDto;
import com.bridgelabz.bookstoreuserservice.model.UserModel;
import com.bridgelabz.bookstoreuserservice.util.Response;
import com.bridgelabz.bookstoreuserservice.util.ResponseToken;

import java.util.List;

public interface IUserService {
    Response createUser(UserDto userDto);

    Response updateUser(Long userId, String token, UserDto userDto);

    List<UserModel> getUserData(String token);

    Response deleteUser(Long userId, String token);

    ResponseToken login(String email, String password);

    Response verify(String token);

    Response resetPassword(String resetPassword, String token);

    Response forgotPassword(String emailId);

    Boolean validate(String token);

    Response sendOtp(String token);

    Response validateOtp(Long otp, String token);

    Response purchaseSubscription(String token);
}
