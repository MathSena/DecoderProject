package com.ead.authuser.models;

import com.ead.authuser.enums.UserStatus;
import com.ead.authuser.enums.UserType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.io.Serial;
import java.util.Set;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.hateoas.RepresentationModel;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "TB_USERS")
public class UserModel extends RepresentationModel<UserModel> implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID userId;
  @Column(nullable = false, unique = true, length = 50)
  private String username;
  @Column(nullable = false, unique = true, length = 50)
  private String email;
  @Column(nullable = false, length = 255)
  @JsonIgnore
  private String password;
  @Column(nullable = false, length = 150)
  private String fullname;
  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private UserStatus userStatus;
  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private UserType userType;
  @Column(length = 20)
  private String phoneNumber;
  @Column(length = 20)
  private String cpf;
  @Column
  private String imageUrl;
  @Column(nullable = false)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
  private LocalDateTime creationDate;
  @Column(nullable = false)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
  private LocalDateTime lastUpdateDate;

  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  @javax.persistence.OneToMany(mappedBy = "user", fetch = javax.persistence.FetchType.LAZY)
  private Set<UserCourseModel> usersCourses;

  public UserCourseModel convertToUserCourseModel(UUID courseId) {
    return new UserCourseModel(null, courseId, this);
  }


}
