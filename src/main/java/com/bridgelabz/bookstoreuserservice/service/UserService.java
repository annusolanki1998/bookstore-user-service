package com.bridgelabz.bookstoreuserservice.service;

import com.bridgelabz.bookstoreuserservice.dto.UserDto;
import com.bridgelabz.bookstoreuserservice.exception.UserNotFoundException;
import com.bridgelabz.bookstoreuserservice.model.UserModel;
import com.bridgelabz.bookstoreuserservice.repository.UserRepository;
import com.bridgelabz.bookstoreuserservice.util.Response;
import com.bridgelabz.bookstoreuserservice.util.ResponseToken;
import com.bridgelabz.bookstoreuserservice.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements IUserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    TokenUtil tokenUtil;
    @Autowired
    MailService mailService;

    @Override
    public Response createUser(UserDto userDto) {
        UserModel userModel = new UserModel(userDto);
        userRepository.save(userModel);
        String body = "User added: " + userModel.getId();
        String subject = "User registration successfully";
        mailService.send(userModel.getEmailId(), body, subject);
        return new Response("Success", 200, userModel);
    }

    @Override
    public Response updateUser(Long userId, String token, UserDto userDto) {
        Long userIdToken = tokenUtil.decodeToken(token);
        Optional<UserModel> isUserPresent = userRepository.findById(userIdToken);
        if (isUserPresent.isPresent()) {
            isUserPresent.get().setFullName(userDto.getFullName());
            isUserPresent.get().setMobile(userDto.getMobile());
            isUserPresent.get().setEmailId(userDto.getEmailId());
            isUserPresent.get().setPassword(userDto.getPassword());
            isUserPresent.get().setDateOfBirth(userDto.getDateOfBirth());
            isUserPresent.get().setUpdatedDate(LocalDateTime.now());
            String body = "Users details updated with id is: " + isUserPresent.get().getId();
            String subject = "Users details updated successfully";
            mailService.send(isUserPresent.get().getEmailId(), body, subject);
            userRepository.save(isUserPresent.get());
            return new Response("Success", 200, isUserPresent.get());
        }
        throw new UserNotFoundException(400, "User Not Present!!!");
    }

    @Override
    public List<UserModel> getUserData(String token) {
        List<UserModel> getalluserdata = userRepository.findAll();
        if (getalluserdata.size() > 0) {
            return getalluserdata;
        }
        throw new UserNotFoundException(400, "User Not Found");
    }

    @Override
    public Response deleteUser(Long userId, String token) {
        Long userIdToken = tokenUtil.decodeToken(token);
        Optional<UserModel> isUserPresent = userRepository.findById(userIdToken);
        if (isUserPresent.isPresent()) {
            userRepository.save(isUserPresent.get());
            String body = "User deleted: " + isUserPresent.get().getId();
            String subject = "User deleted successfully";
            mailService.send(isUserPresent.get().getEmailId(), body, subject);
            return new Response("Success", 200, isUserPresent.get());
        }
        throw new UserNotFoundException(400, "User does not found");
    }

    @Override
    public ResponseToken login(String email, String password) {
        Optional<UserModel> isUserPresent = userRepository.findByEmailId(email);
        if (isUserPresent.isPresent()) {
            if (isUserPresent.get().getPassword().equals(password)) {
                String token = tokenUtil.createToken(isUserPresent.get().getId());
                return new ResponseToken(200, "success", token);
            }
            throw new UserNotFoundException(400, "Invalid Credentials");
        }
        throw new UserNotFoundException(400, "User Not Found");
    }

    @Override
    public Response verify(String token) {
        Long id = tokenUtil.decodeToken(token);
        Optional<UserModel> isUserPresent = userRepository.findById(id);
        if (isUserPresent.isPresent() != true) {
            isUserPresent.get().setVerify(true);
            userRepository.save(isUserPresent.get());
            return new Response("success", 200, isUserPresent.get());
        }
        throw new UserNotFoundException(400, "Not Found");
    }

    @Override
    public Response resetPassword(String resetPassword, String token) {
        Long id = tokenUtil.decodeToken(token);
        Optional<UserModel> isUserPresent = userRepository.findById(id);
        if (isUserPresent.isPresent()) {
            isUserPresent.get().setPassword(resetPassword);
            userRepository.save(isUserPresent.get());
            return new Response("Success", 200, isUserPresent.get());
        }
        throw new UserNotFoundException(400, "Not found");
    }

    @Override
    public Response forgotPassword(String emailId) {
        Optional<UserModel> isEmailPresent = userRepository.findByEmailId(emailId);
        if (isEmailPresent.isPresent()) {
            String token = tokenUtil.createToken(isEmailPresent.get().getId());
            String url = "http://localhost:8082/user/forgotPassword/";
            String subject = "forgot password";
            String body = "For reset password click this link" + url + "use this to reset" + token;
            mailService.send(isEmailPresent.get().getEmailId(), subject, body);
            return new Response("Success", 200, isEmailPresent.get());
        }
        throw new UserNotFoundException(400, "Not found");
    }

    @Override
    public Response sendOtp(String token) {
        Long userId = tokenUtil.decodeToken(token);
        Optional<UserModel> isUserPresent = userRepository.findById(userId);
        if (isUserPresent.isPresent()) {
            long min = 100000, max = 999999;
            long random_long = (long) (Math.random() * (max - min + 1) + min);
            isUserPresent.get().setOtp(random_long);
            isUserPresent.get().setUpdatedDate(LocalDateTime.now());
            userRepository.save(isUserPresent.get());
            String body = isUserPresent.get().getOtp() + " is your OTP";
            String subject = "Otp verification";
            mailService.send(isUserPresent.get().getEmailId(), subject, body);
            return new Response("success", 200, isUserPresent.get());
        }
        throw new UserNotFoundException(500, "Invalid UserId");
    }

    @Override
    public Response validateOtp(Long otp, String token) {
        Long userId = tokenUtil.decodeToken(token);
        Optional<UserModel> isUserPresent = userRepository.findById(userId);
        if (isUserPresent.isPresent()) {
            if (isUserPresent.get().getOtp() == otp) {
                isUserPresent.get().setVerify(true);
                return new Response("success", 200, isUserPresent.get());
            }
            throw new UserNotFoundException(400, "Not found");
        }
        throw new UserNotFoundException(400, "Token is wrong");
    }

    @Override
    public Response purchaseSubscription(String token) {
        Long userId = tokenUtil.decodeToken(token);
        Optional<UserModel> isUserPresent = userRepository.findById(userId);
        if (isUserPresent.isPresent()) {
            isUserPresent.get().setPurchaseDate(new Date());
            Date date = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.MONTH, 12);
            Date expireDate = calendar.getTime();
            isUserPresent.get().setExpiryDate(expireDate);
            userRepository.save(isUserPresent.get());
            return new Response("success", 200, isUserPresent.get());
        }
        throw new UserNotFoundException(400, "User Not Found");
    }

    @Override
    public Boolean validate(String token) {
        Long userId = tokenUtil.decodeToken(token);
        Optional<UserModel> isUser = userRepository.findById(userId);
        if (isUser.isPresent()) {
            return true;
        }
        return false;
    }

}
