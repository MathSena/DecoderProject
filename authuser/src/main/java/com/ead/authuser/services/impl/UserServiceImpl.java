package com.ead.authuser.services.impl;

import com.ead.authuser.models.UserModel;
import com.ead.authuser.repositories.UserRepository;
import com.ead.authuser.services.UserService;
import com.ead.authuser.specifications.SpecificationTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  @Override
  public List<UserModel> getAllUsers() {
    log.info("Fetching all users.");
    List<UserModel> users = userRepository.findAll();
    log.info("Successfully fetched {} users.", users.size());
    return users;
  }

  @Override
  public Optional<UserModel> getUserById(UUID userId) {
    log.info("Fetching user with ID: {}", userId);
    Optional<UserModel> user = userRepository.findById(userId);
    if (user.isPresent()) {
      log.info("User with ID: {} found.", userId);
    } else {
      log.warn("User with ID: {} not found.", userId);
    }
    return user;
  }

  @Override
  public void deleteUserById(UUID userId) {
    log.info("Deleting user with ID: {}", userId);
    Optional<UserModel> userModelOptional = userRepository.findById(userId);
    if (userModelOptional.isPresent()) {
      userRepository.deleteById(userId);
      log.info("User with ID: {} successfully deleted.", userId);
    } else {
      log.warn("User with ID: {} not found.", userId);
      throw new RuntimeException("User not found");
    }
  }

  @Override
  public void save(UserModel userModel) {
    log.info("Saving user with username: {}", userModel.getUsername());
    userRepository.save(userModel);
    log.info("User with username: {} successfully saved.", userModel.getUsername());
  }

  @Override
  public Optional<Object> getUserByUsername(String username) {
    log.info("Fetching user by username: {}", username);
    Optional<Object> user = userRepository.findByUsername(username);
    if (user.isPresent()) {
      log.info("User with username: {} found.", username);
    } else {
      log.warn("User with username: {} not found.", username);
    }
    return user;
  }

  @Override
  public Optional<Object> getUserByEmail(String email) {
    log.info("Fetching user by email: {}", email);
    Optional<Object> user = userRepository.findByEmail(email);
    if (user.isPresent()) {
      log.info("User with email: {} found.", email);
    } else {
      log.warn("User with email: {} not found.", email);
    }
    return user;
  }

  @Override
  public Page<UserModel> getAllUsers(Pageable pageable, SpecificationTemplate.UserSpec spec) {
    log.info("Fetching users with pagination and specification.");
    Page<UserModel> userPage = userRepository.findAll(pageable, spec);
    log.info("Successfully fetched {} users with pagination.", userPage.getTotalElements());
    return userPage;
  }
}