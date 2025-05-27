package com.ead.course.repositories;

import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LessonRepository extends JpaRepository<LessonModel, UUID> {

  @Query(value = "SELECT l FROM tb_lessons l WHERE l.module.moduleId = :moduleId", nativeQuery = true)
  List<LessonModel> findAllLessonsIntoModule(@Param("moduleId") UUID moduleId);


}
