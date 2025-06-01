package com.ead.authuser.controllers;

import com.ead.authuser.dtos.UserDto;
import com.ead.authuser.enums.UserStatus;
import com.ead.authuser.enums.UserType;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.services.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

  private final UserService userService;

  @PostMapping("/signup")
  public ResponseEntity<Object> signUp(
      @RequestBody @Validated(UserDto.UserView.RegistrationPost.class) @JsonView(UserDto.UserView.RegistrationPost.class)
      UserDto userDto) {

    log.info("Starting user registration process for username: {}", userDto.getUsername());

    if (userService.getUserByUsername(userDto.getUsername()).isPresent()) {
      log.warn("Conflict: Username '{}' is already in use!", userDto.getUsername());
      return ResponseEntity.status(HttpStatus.CONFLICT)
          .body("Conflict: Username is already in use!");
    }

    if (userService.getUserByEmail(userDto.getEmail()).isPresent()) {
      log.warn("Conflict: Email '{}' is already in use!", userDto.getEmail());
      return ResponseEntity.status(HttpStatus.CONFLICT)
          .body("Conflict: Email is already in use!");
    }

    log.info("Creating new user with username: {}", userDto.getUsername());
    UserModel userModel = createUserModelFromDto(userDto);

    userService.save(userModel);
    log.info("User '{}' successfully registered.", userModel.getUsername());

    return ResponseEntity.status(HttpStatus.CREATED).body(userModel);
  }

  private UserModel createUserModelFromDto(UserDto userDto) {
    UserModel userModel = new UserModel();
    BeanUtils.copyProperties(userDto, userModel);
    userModel.setUserStatus(UserStatus.ACTIVE);
    userModel.setUserType(UserType.STUDENT);
    userModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
    userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
    return userModel;
  }
}