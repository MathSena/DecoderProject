package com.ead.course.services.impl;

import com.ead.course.models.LessonModel;
import com.ead.course.repositories.LessonRepository;
import com.ead.course.services.LessonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class LessonServiceImpl implements LessonService {

  @Autowired
  LessonRepository lessonRepository;

  @Override
  public LessonModel saveLesson(LessonModel lessonModel) {
    log.info("Saving lesson with ID: {}", lessonModel.getLessonId());
    LessonModel savedLesson = lessonRepository.save(lessonModel);
    log.info("Lesson with ID: {} successfully saved.", savedLesson.getLessonId());
    return savedLesson;
  }

  @Override
  public Optional<LessonModel> findLessonIntoModule(UUID moduleId, UUID lessonId) {
    log.info("Fetching lesson with ID: {} in module ID: {}", lessonId, moduleId);
    Optional<LessonModel> lesson = lessonRepository.findAllLessonsIntoModule(moduleId, lessonId);
    if (lesson.isPresent()) {
      log.info("Lesson with ID: {} found in module ID: {}", lessonId, moduleId);
    } else {
      log.warn("Lesson with ID: {} not found in module ID: {}", lessonId, moduleId);
    }
    return lesson;
  }

  @Override
  public void deleteLesson(LessonModel lessonModel) {
    log.info("Deleting lesson with ID: {}", lessonModel.getLessonId());
    lessonRepository.delete(lessonModel);
    log.info("Lesson with ID: {} successfully deleted.", lessonModel.getLessonId());
  }

  @Override
  public List<LessonModel> findAllByModule(UUID moduleId) {
    log.info("Fetching all lessons for module ID: {}", moduleId);
    List<LessonModel> lessons = lessonRepository.findAllLessonsIntoModule(moduleId);
    log.info("Successfully fetched {} lessons for module ID: {}", lessons.size(), moduleId);
    return lessons;
  }

  @Override
  public Page<LessonModel> findAllByModule(Specification<LessonModel> spec, Pageable pageable) {
    log.info("Fetching all lessons with specification and pagination.");
    Page<LessonModel> lessons = lessonRepository.findAll(spec, pageable);
    log.info("Successfully fetched {} lessons.", lessons.getTotalElements());
    return lessons;
  }
}