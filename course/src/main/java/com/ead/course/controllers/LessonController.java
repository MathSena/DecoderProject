package com.ead.course.controllers;

import com.ead.course.dtos.LessonDto;
import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.services.LessonService;
import com.ead.course.services.ModuleService;
import com.ead.course.specifications.SpecificationTemplate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class LessonController {

  @Autowired
  LessonService lessonService;

  @Autowired
  ModuleService moduleService;

  @GetMapping("/modules/{moduleId}/lessons")
  public ResponseEntity<Page<LessonModel>> getAllLessonsIntoModule(
      @PathVariable(value = "moduleId") UUID moduleId, SpecificationTemplate.LessonSpec spec,
      @PageableDefault(size = 10, page = 0, sort = "lessonId", direction = Sort.Direction.ASC) Pageable pageable) {
    return ResponseEntity.status(200).body(lessonService.findAllByModule(SpecificationTemplate.lessonBelongsToModule(moduleId)
        .and(spec), pageable));
  }

  @GetMapping("/modules/{moduleId}/lessons/{lessonId}")
  public ResponseEntity<Object> getOneLessonIntoModule(
      @PathVariable(value = "moduleId") UUID moduleId,
      @PathVariable(value = "lessonId") UUID lessonId) {
    Optional<LessonModel> lessonModelOptional = lessonService.findLessonIntoModule(moduleId,
        lessonId);

    return lessonModelOptional.<ResponseEntity<Object>>map(
            lessonModel -> ResponseEntity.status(200).body(lessonModel))
        .orElseGet(
            () -> ResponseEntity.status(404).body("Lesson not found in the specified module."));
  }

  @PostMapping("/modules/{moduleId}/lessons")
  public ResponseEntity<Object> saveLesson(@PathVariable(value = "moduleId") UUID moduleId,
      LessonDto lessonDto) {
    Optional<ModuleModel> moduleModelOptional = moduleService.findById(moduleId);

    if (moduleModelOptional.isEmpty()) {
      return ResponseEntity.status(404).body("Module not found.");
    }
    var lessonModel = new LessonModel();
    BeanUtils.copyProperties(lessonDto, lessonModel);
    lessonModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
    lessonModel.setModule(moduleModelOptional.get());
    return ResponseEntity.status(201).body(
        lessonService.saveLesson(lessonModel));
  }

  @DeleteMapping("/modules/{moduleId}lessons/{lessonId}")
  public ResponseEntity<Object> deleteLesson(@PathVariable(value = "moduleId") UUID moduleId,
      @PathVariable(value = "lessonId") UUID lessonId) {
    Optional<LessonModel> lessonModelOptional = lessonService.findLessonIntoModule(moduleId,
        lessonId);

    if (lessonModelOptional.isEmpty()) {
      return ResponseEntity.status(404).body("Lesson not found in the specified module.");
    }
    lessonService.deleteLesson(lessonModelOptional.get());
    return ResponseEntity.status(200).body("Lesson deleted successfully.");

  }

  @PutMapping("/modules/{moduleId}/lessons/{lessonId}")
  public ResponseEntity<Object> updateLesson(@PathVariable(value = "moduleId") UUID moduleId,
      @PathVariable(value = "lessonId") UUID lessonId, LessonDto lessonDto) {
    Optional<LessonModel> lessonModelOptional = lessonService.findLessonIntoModule(moduleId,
        lessonId);

    if (lessonModelOptional.isEmpty()) {
      return ResponseEntity.status(404).body("Lesson not found in the specified module.");
    }
    var lessonModel = lessonModelOptional.get();
    BeanUtils.copyProperties(lessonDto, lessonModel);
    return ResponseEntity.status(200).body(
        lessonService.saveLesson(lessonModel));
  }

}
