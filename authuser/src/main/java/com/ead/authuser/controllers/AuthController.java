package com.ead.authuser.controllers;

import com.ead.authuser.dtos.UserDto;
import com.ead.authuser.enums.UserStatus;
import com.ead.authuser.enums.UserType;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.services.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<Object> signUp(@RequestBody @JsonView(UserDto.UserView.RegistrationPost.class)
                                             UserDto userDto) {

        if (userService.getUserByUsername(userDto.getUsername())
                .isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Conflict: Username is already in use!");
        }
        if (userService.getUserByEmail(userDto.getEmail())
                .isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Conflict: Email is already in use!");
        }

        var userModel = new UserModel();
        BeanUtils.copyProperties(userDto, userModel);
        userModel.setUserStatus(UserStatus.ACTIVE);
        userModel.setUserType(UserType.STUDENT);
        userModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        userService.save(userModel);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userModel);
    }
}
