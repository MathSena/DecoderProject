package com.ead.authuser.controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.ead.authuser.dtos.UserDto;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.services.UserService;
import com.ead.authuser.specifications.SpecificationTemplate;
import com.ead.authuser.specifications.SpecificationTemplate.UserSpec;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

  private final UserService userService;

  @GetMapping
  public ResponseEntity<List<UserModel>> getAllUsers(
      SpecificationTemplate.UserSpec spec,
      @PageableDefault(size = 10, page = 0, sort = "userId", direction = Sort.Direction.DESC) Pageable pageable,
      @RequestParam(required = false) UUID courseId) {
    log.info("Fetching all users with pagination and specification.");
    Page<UserModel> userPage = null;
    if (courseId != null) {
      log.info("Fetching users for course ID: {}", courseId);
      userPage = userService.getAllUsers(
          (Pageable) SpecificationTemplate.userCourseId(courseId).and(spec),
          (UserSpec) pageable);
    } else {
      log.info("Fetching all users without course filter.");
      userPage = userService.getAllUsers(pageable, spec);
    }
    List<UserModel> users = userService.getAllUsers();
    if (!userPage.isEmpty()) {
      userPage.getContent().forEach(user ->
          user.add(
              linkTo(methodOn(UserController.class).getUserById(user.getUserId())).withSelfRel())
      );
    }
    log.info("Successfully fetched {} users.", users.size());
    return ResponseEntity.status(HttpStatus.OK).body(users);
  }

  @GetMapping("/{userId}")
  public ResponseEntity<UserModel> getUserById(@PathVariable(value = "userId") UUID userId) {
    log.info("Fetching user with ID: {}", userId);
    Optional<UserModel> userModelOptional = userService.getUserById(userId);
    return userModelOptional.map(userModel -> {
      log.info("User with ID: {} found.", userId);
      return ResponseEntity.status(HttpStatus.OK).body(userModel);
    }).orElseGet(() -> {
      log.warn("User with ID: {} not found.", userId);
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    });
  }

  @DeleteMapping("/{userId}")
  public ResponseEntity<Void> deleteUserById(@PathVariable(value = "userId") UUID userId) {
    log.info("Deleting user with ID: {}", userId);
    Optional<UserModel> userModelOptional = userService.getUserById(userId);
    if (userModelOptional.isPresent()) {
      userService.deleteUserById(userId);
      log.info("User with ID: {} successfully deleted.", userId);
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    } else {
      log.warn("User with ID: {} not found.", userId);
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

  @PutMapping("/{userId}")
  public ResponseEntity<Object> updateUser(@PathVariable(value = "userId") UUID userId,
      @RequestBody @Validated(UserDto.UserView.UserPut.class)
      @JsonView(UserDto.UserView.UserPut.class) UserDto userDto) {
    log.info("Updating user with ID: {}", userId);
    Optional<UserModel> userModelOptional = userService.getUserById(userId);
    if (userModelOptional.isPresent()) {
      var userModel = userModelOptional.get();
      userModel.setFullname(userDto.getFullname());
      userModel.setPhoneNumber(userDto.getPhoneNumber());
      userModel.setCpf(userDto.getCpf());
      userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
      userService.save(userModel);
      log.info("User with ID: {} successfully updated.", userId);
      return ResponseEntity.status(HttpStatus.OK).body(userDto);
    } else {
      log.warn("User with ID: {} not found.", userId);
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

  @PutMapping("/{userId}/password")
  public ResponseEntity<Object> updatePassword(@PathVariable(value = "userId") UUID userId,
      @RequestBody @Validated(UserDto.UserView.PasswordPut.class)
      @JsonView(UserDto.UserView.PasswordPut.class) UserDto userDto) {
    log.info("Updating password for user with ID: {}", userId);
    Optional<UserModel> userModelOptional = userService.getUserById(userId);
    if (userModelOptional.isPresent()) {
      var userModel = userModelOptional.get();
      if (!userModel.getPassword().equals(userDto.getOldPassword())) {
        log.warn("Old password for user with ID: {} is incorrect.", userId);
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body("Conflict: Old password is incorrect!");
      }
      userModel.setPassword(userDto.getPassword());
      userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
      userService.save(userModel);
      log.info("Password for user with ID: {} successfully updated.", userId);
      return ResponseEntity.status(HttpStatus.OK).body(userDto);
    } else {
      log.warn("User with ID: {} not found.", userId);
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

  @PutMapping("/{userId}/image")
  public ResponseEntity<Object> updateImage(@PathVariable(value = "userId") UUID userId,
      @RequestBody @Validated(UserDto.UserView.ImagePut.class)
      @JsonView(UserDto.UserView.ImagePut.class) UserDto userDto) {
    log.info("Updating image for user with ID: {}", userId);
    Optional<UserModel> userModelOptional = userService.getUserById(userId);
    if (userModelOptional.isPresent()) {
      var userModel = userModelOptional.get();
      userModel.setImageUrl(userDto.getImageUrl());
      userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
      userService.save(userModel);
      log.info("Image for user with ID: {} successfully updated.", userId);
      return ResponseEntity.status(HttpStatus.OK).body(userDto);
    } else {
      log.warn("User with ID: {} not found.", userId);
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }
}