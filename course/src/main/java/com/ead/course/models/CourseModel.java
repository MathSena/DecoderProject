package com.ead.course.models;

import com.ead.course.enums.CourseLevel;
import com.ead.course.enums.CourseStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "tb_courses")
public class CourseModel implements Serializable {

  private static final long serialVersionUID = 1l;
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
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
  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private CourseStatus courseStatus;
  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private CourseLevel courseLevel;
  @Column(nullable = false)
  private UUID userInstructor;
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  @OneToMany(mappedBy = "course")
  private Set<ModuleModel> modules;


}
