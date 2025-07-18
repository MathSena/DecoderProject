package com.ead.authuser.controllers;

import com.ead.authuser.dtos.InstructorDto;
import com.ead.authuser.enums.UserType;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.services.UserService;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/instructors")
public class InstructorController {

  @Autowired
  UserService userService;

  @PostMapping("/subscription")
  public ResponseEntity<Object> saveSubscriptionInstructor(
      @RequestBody @Valid InstructorDto instructorDto) {
    Optional<UserModel> userModelOptional = userService.getUserById(instructorDto.getUserId());
    if (userModelOptional.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
    } else {
      var userModel = userModelOptional.get();
      userModel.setUserType(UserType.INSTRUCTOR);
      userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
      userService.save(userModel);
      return ResponseEntity.status(HttpStatus.OK).body(userModel);
    }
  }

}
