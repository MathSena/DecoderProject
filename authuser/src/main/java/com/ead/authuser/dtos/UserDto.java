package com.ead.authuser.dtos;

import com.ead.authuser.validation.UsernameConstraint;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {

  public interface UserView {

    public static interface RegistrationPost {

    }

    public static interface UserPut {

    }

    public static interface PasswordPut {

    }

    public static interface ImagePut {

    }

  }

  @NotBlank(groups = {UserView.RegistrationPost.class})
  @JsonView(UserView.RegistrationPost.class)
  @Size(min = 4, max = 50, groups = {UserView.RegistrationPost.class})
  @UsernameConstraint(groups = {UserView.RegistrationPost.class})
  private String username;
  @NotBlank(groups = {UserView.RegistrationPost.class})
  @Email(groups = {UserView.RegistrationPost.class})
  @JsonView(UserView.RegistrationPost.class)
  private String email;
  @NotBlank(groups = {UserView.RegistrationPost.class, UserView.PasswordPut.class})
  @JsonView({UserView.RegistrationPost.class, UserView.PasswordPut.class})
  @Size(min = 6, max = 20, groups = {UserView.PasswordPut.class, UserView.RegistrationPost.class})
  private String password;
  @NotBlank(groups = {UserView.PasswordPut.class})
  @JsonView(UserView.PasswordPut.class)
  @Size(min = 6, max = 20, groups = {UserView.PasswordPut.class})
  private String oldPassword;
  @JsonView({UserView.RegistrationPost.class, UserView.UserPut.class})
  private String fullname;
  @JsonView({UserView.RegistrationPost.class, UserView.UserPut.class})
  private String phoneNumber;
  @JsonView({UserView.RegistrationPost.class, UserView.UserPut.class})
  private String cpf;
  @JsonView(UserView.ImagePut.class)
  private String imageUrl;

}
