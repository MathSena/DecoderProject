package com.ead.course.repositories;

import com.ead.course.models.LessonModel;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LessonRepository extends JpaRepository<LessonModel, UUID> {

  // Custom query methods can be defined here if needed

}
