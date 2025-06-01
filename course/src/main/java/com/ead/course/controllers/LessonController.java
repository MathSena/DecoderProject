package com.ead.course.controllers;

import com.ead.course.dtos.LessonDto;
import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.services.LessonService;
import com.ead.course.services.ModuleService;
import com.ead.course.specifications.SpecificationTemplate;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@Slf4j
public class LessonController {

  @Autowired
  LessonService lessonService;

  @Autowired
  ModuleService moduleService;

  @GetMapping("/modules/{moduleId}/lessons")
  public ResponseEntity<Page<LessonModel>> getAllLessonsIntoModule(
      @PathVariable(value = "moduleId") UUID moduleId, SpecificationTemplate.LessonSpec spec,
      @PageableDefault(size = 10, page = 0, sort = "lessonId", direction = Sort.Direction.ASC) Pageable pageable) {
    log.info("Fetching all lessons for module ID: {}", moduleId);
    Page<LessonModel> lessons = lessonService.findAllByModule(
        SpecificationTemplate.lessonBelongsToModule(moduleId).and(spec), pageable);
    log.info("Successfully fetched {} lessons for module ID: {}", lessons.getTotalElements(),
        moduleId);
    return ResponseEntity.status(200).body(lessons);
  }

  @GetMapping("/modules/{moduleId}/lessons/{lessonId}")
  public ResponseEntity<Object> getOneLessonIntoModule(
      @PathVariable(value = "moduleId") UUID moduleId,
      @PathVariable(value = "lessonId") UUID lessonId) {
    log.info("Fetching lesson with ID: {} in module ID: {}", lessonId, moduleId);
    Optional<LessonModel> lessonModelOptional = lessonService.findLessonIntoModule(moduleId,
        lessonId);

    if (lessonModelOptional.isPresent()) {
      log.info("Lesson with ID: {} found in module ID: {}", lessonId, moduleId);
      return ResponseEntity.status(200).body(lessonModelOptional.get());
    } else {
      log.warn("Lesson with ID: {} not found in module ID: {}", lessonId, moduleId);
      return ResponseEntity.status(404).body("Lesson not found in the specified module.");
    }
  }

  @PostMapping("/modules/{moduleId}/lessons")
  public ResponseEntity<Object> saveLesson(
      @PathVariable(value = "moduleId") UUID moduleId,
      @RequestBody @Valid LessonDto lessonDto) {
    log.info("Saving new lesson in module ID: {}", moduleId);
    Optional<ModuleModel> moduleModelOptional = moduleService.findById(moduleId);

    if (moduleModelOptional.isEmpty()) {
      log.warn("Module with ID: {} not found.", moduleId);
      return ResponseEntity.status(404).body("Module not found.");
    }

    var lessonModel = new LessonModel();
    BeanUtils.copyProperties(lessonDto, lessonModel);
    lessonModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
    lessonModel.setModule(moduleModelOptional.get());
    lessonService.saveLesson(lessonModel);
    log.info("Lesson successfully saved in module ID: {}", moduleId);
    return ResponseEntity.status(201).body(lessonModel);
  }

  @DeleteMapping("/modules/{moduleId}/lessons/{lessonId}")
  public ResponseEntity<Object> deleteLesson(
      @PathVariable(value = "moduleId") UUID moduleId,
      @PathVariable(value = "lessonId") UUID lessonId) {
    log.info("Deleting lesson with ID: {} in module ID: {}", lessonId, moduleId);
    Optional<LessonModel> lessonModelOptional = lessonService.findLessonIntoModule(moduleId,
        lessonId);

    if (lessonModelOptional.isEmpty()) {
      log.warn("Lesson with ID: {} not found in module ID: {}", lessonId, moduleId);
      return ResponseEntity.status(404).body("Lesson not found in the specified module.");
    }

    lessonService.deleteLesson(lessonModelOptional.get());
    log.info("Lesson with ID: {} successfully deleted from module ID: {}", lessonId, moduleId);
    return ResponseEntity.status(200).body("Lesson deleted successfully.");
  }

  @PutMapping("/modules/{moduleId}/lessons/{lessonId}")
  public ResponseEntity<Object> updateLesson(
      @PathVariable(value = "moduleId") UUID moduleId,
      @PathVariable(value = "lessonId") UUID lessonId,
      @RequestBody @Valid LessonDto lessonDto) {
    log.info("Updating lesson with ID: {} in module ID: {}", lessonId, moduleId);
    Optional<LessonModel> lessonModelOptional = lessonService.findLessonIntoModule(moduleId,
        lessonId);

    if (lessonModelOptional.isEmpty()) {
      log.warn("Lesson with ID: {} not found in module ID: {}", lessonId, moduleId);
      return ResponseEntity.status(404).body("Lesson not found in the specified module.");
    }

    var lessonModel = lessonModelOptional.get();
    BeanUtils.copyProperties(lessonDto, lessonModel);
    lessonService.saveLesson(lessonModel);
    log.info("Lesson with ID: {} successfully updated in module ID: {}", lessonId, moduleId);
    return ResponseEntity.status(200).body(lessonModel);
  }
}