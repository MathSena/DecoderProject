package com.ead.course.repositories;

import com.ead.course.models.CourseModel;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<CourseModel, UUID> {

  // Custom query methods can be defined here if needed

}
