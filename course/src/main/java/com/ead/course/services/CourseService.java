package com.ead.course.services;

import com.ead.course.models.CourseModel;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface CourseService {

  void deleteCourse(CourseModel courseModel);

  CourseModel saveCourse(CourseModel courseModel);

  Optional<CourseModel> findById(UUID courseID);

  Page<CourseModel> findAll(Specification<CourseModel> spec, Pageable pageable);
}
