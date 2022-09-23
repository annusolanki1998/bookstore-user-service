package com.bridgelabz.bookstoreuserservice.model;

import com.bridgelabz.bookstoreuserservice.dto.UserDto;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "User")
@Data
public class UserModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String fullName;
    private long mobile;
    private String emailId;
    private String password;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private boolean isActive;
    private boolean isDeleted;
    private Date dateOfBirth;
    private boolean isVerify;
    private long otp;
    private Date purchaseDate;
    private Date expiryDate;

    public UserModel(UserDto userDto) {
        this.fullName = userDto.getFullName();
        this.mobile = userDto.getMobile();
        this.emailId = userDto.getEmailId();
        this.password = userDto.getPassword();
        this.dateOfBirth = userDto.getDateOfBirth();
        this.otp = userDto.getOtp();
    }

    public UserModel() {

    }
}
