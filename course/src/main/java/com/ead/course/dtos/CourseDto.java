package com.ead.course.dtos;

import com.ead.course.enums.CourseLevel;
import com.ead.course.enums.CourseStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Data;

@Data
public class CourseDto {

  @NotBlank
  private String courseName;
  @NotBlank
  private String courseDescription;
  private String courseImageUrl;
  @NotNull
  private CourseStatus courseStatus;
  @NotNull
  private UUID courseInstructorId;
  @NotNull
  private CourseLevel courseLevel;

}
