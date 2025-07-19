package com.ead.authuser.services;

import com.ead.authuser.models.UserModel;
import com.ead.authuser.specifications.SpecificationTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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

  Page<UserModel> getAllUsers(Pageable pageable, SpecificationTemplate.UserSpec spec);

  boolean existsByUsername(String username);

  boolean existsByEmail(String email);

}
