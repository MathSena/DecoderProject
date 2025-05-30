package com.ead.course.services;

import com.ead.course.models.LessonModel;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface LessonService {

  LessonModel saveLesson(LessonModel lessonModel);

  Optional<LessonModel> findLessonIntoModule(UUID moduleId, UUID lessonId);

  void deleteLesson(LessonModel lessonModel);


  List<LessonModel> findAllByModule(UUID moduleId);

  Page<LessonModel> findAllByModule(Specification<LessonModel> spec, Pageable pageable);
}
