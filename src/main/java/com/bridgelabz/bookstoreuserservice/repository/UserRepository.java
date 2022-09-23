package com.bridgelabz.bookstoreuserservice.repository;


import com.bridgelabz.bookstoreuserservice.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserModel, Long> {
    Optional<UserModel> findByEmailId(String email);

}