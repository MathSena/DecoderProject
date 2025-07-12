package com.ead.course.models;

import com.ead.course.enums.CourseLevel;
import com.ead.course.enums.CourseStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "tb_courses")
public class CourseModel implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "course_id")
  private UUID courseId;

  @Column(nullable = false, unique = true, length = 150)
  private String courseName;

  @Column(nullable = false, unique = true, length = 150)
  private String courseDescription;

  @Column
  private String courseImageUrl;

  @Column(nullable = false)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
  private LocalDateTime creationDate;

  @Column(nullable = false)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
  private LocalDateTime lastUpdateDate;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private CourseStatus courseStatus;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private CourseLevel courseLevel;

  @Column(nullable = false, name = "user_instructor")
  private UUID userInstructor;

  // Relacionamento com CourseUserModel (muitos CourseUserModel para um CourseModel)
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  @OneToMany(mappedBy = "course", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  @Fetch(FetchMode.SUBSELECT)
  private Set<CourseUserModel> coursesUsers;

  // Relacionamento com ModuleModel (caso você tenha a entidade ModuleModel)
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  @OneToMany(mappedBy = "course", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  @Fetch(FetchMode.SUBSELECT)
  private Set<ModuleModel> modules;

  // Método utilitário
  public CourseUserModel convertToCourseUserModel(UUID userID) {
    return new CourseUserModel(null, userID, this);
  }
}
