package com.ead.authuser.services;

import com.ead.authuser.models.UserModel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {

    List<UserModel> getAllUsers();

    Optional<UserModel> getUserById(UUID userId);

    void deleteUserById(UUID userId);

    void save(UserModel userModel);

    Optional<Object> getUserByUsername(String username);

    Optional<Object> getUserByEmail(String email);
}
