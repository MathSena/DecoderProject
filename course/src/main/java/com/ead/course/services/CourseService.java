package com.ead.course.services;

import com.ead.course.models.CourseModel;
import com.ead.course.specifications.SpecificationTemplate.CourseSpec;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CourseService {

  void deleteCourse(CourseModel courseModel);

  CourseModel saveCourse(CourseModel courseModel);

  Optional<CourseModel> findById(UUID courseID);


  Page<CourseModel> findAll(CourseSpec spec, Pageable pageable);
}
