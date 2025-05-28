package com.ead.course.services;

import com.ead.course.models.CourseModel;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CourseService {

  void deleteCourse(CourseModel courseModel);

  CourseModel saveCourse(CourseModel courseModel);

  Optional<CourseModel> findById(UUID courseID);

  List<CourseModel> findAll();
}
