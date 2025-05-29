package com.ead.course.repositories;

import com.ead.course.models.LessonModel;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LessonRepository extends JpaRepository<LessonModel, UUID> {

  @Query(value = "SELECT l FROM tb_lessons l WHERE l.module.moduleId = :moduleId", nativeQuery = true)
  List<LessonModel> findAllLessonsIntoModule(@Param("moduleId") UUID moduleId);

  @Query(value = "SELECT l FROM tb_lessons l WHERE l.module.moduleId = :moduleId AND l.lessonId = :lessonId", nativeQuery = true)
  Optional<LessonModel> findAllLessonsIntoModule(@Param("moduleId") UUID moduleId,
      @Param("lessonId") UUID lessonId);


}
