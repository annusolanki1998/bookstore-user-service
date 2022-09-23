package com.bridgelabz.bookstoreuserservice.dto;

import lombok.Data;

import java.util.Date;

@Data
public class UserDto {
    private String fullName;
    private long mobile;
    private String emailId;
    private String profilePic;
    private String password;
    private Date dateOfBirth;
    private long otp;
}
