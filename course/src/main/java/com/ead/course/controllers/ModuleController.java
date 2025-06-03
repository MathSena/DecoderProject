package com.ead.course.controllers;

import com.ead.course.dtos.ModuleDto;
import com.ead.course.models.CourseModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.services.CourseService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@Slf4j
public class ModuleController {

  @Autowired
  ModuleService moduleService;

  @Autowired
  CourseService courseService;

  @GetMapping("/courses/{courseId}/modules")
  public ResponseEntity<Page<ModuleModel>> getAllModules(
      @PathVariable(value = "courseId") UUID courseId,
      SpecificationTemplate.ModuleSpec spec,
      @PageableDefault(size = 10, page = 0, sort = "moduleId", direction = Sort.Direction.ASC) Pageable pageable) {
    log.info("Fetching all modules for course ID: {}", courseId);
    Page<ModuleModel> modules = moduleService.findAllByCourse(
        SpecificationTemplate.moduleBelongsToCourse(courseId).and(spec), pageable);
    log.info("Successfully fetched {} modules for course ID: {}", modules.getTotalElements(),
        courseId);
    return ResponseEntity.status(HttpStatus.OK).body(modules);
  }

  @GetMapping("/courses/{courseId}/modules/{moduleId}")
  public ResponseEntity<Object> getOneModule(@PathVariable(value = "courseId") UUID courseId,
      @PathVariable(value = "moduleId") UUID moduleId) {
    log.info("Fetching module with ID: {} in course ID: {}", moduleId, courseId);
    Optional<ModuleModel> moduleModelOptional = moduleService.findModuleIntoCourse(courseId,
        moduleId);

    if (moduleModelOptional.isPresent()) {
      log.info("Module with ID: {} found in course ID: {}", moduleId, courseId);
      return ResponseEntity.status(HttpStatus.OK).body(moduleModelOptional.get());
    } else {
      log.warn("Module with ID: {} not found in course ID: {}", moduleId, courseId);
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module not found.");
    }
  }

  @PostMapping("/courses/{courseId}/modules")
  public ResponseEntity<Object> saveModule(@PathVariable(value = "courseId") UUID courseId,
      @RequestBody @Valid ModuleDto moduleDto) {
    log.info("Saving new module in course ID: {}", courseId);
    Optional<CourseModel> courseModelOptional = courseService.findById(courseId);

    if (courseModelOptional.isEmpty()) {
      log.warn("Course with ID: {} not found.", courseId);
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found.");
    }

    var moduleModel = new ModuleModel();
    BeanUtils.copyProperties(moduleDto, moduleModel);
    moduleModel.setModuleName(moduleDto.getTitle());
    moduleModel.setModuleDescription(moduleDto.getDescription());
    moduleModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
    moduleModel.setCourse(courseModelOptional.get());
    moduleService.saveModule(moduleModel);
    log.info("Module successfully saved in course ID: {}", courseId);
    return ResponseEntity.status(HttpStatus.CREATED).body(moduleModel);
  }

  @DeleteMapping("/courses/{courseId}/modules/{moduleId}")
  public ResponseEntity<Object> deleteModule(@PathVariable(value = "courseId") UUID courseId,
      @PathVariable(value = "moduleId") UUID moduleId) {
    log.info("Deleting module with ID: {} in course ID: {}", moduleId, courseId);
    Optional<ModuleModel> moduleModelOptional = moduleService.findModuleIntoCourse(courseId,
        moduleId);

    if (moduleModelOptional.isEmpty()) {
      log.warn("Module with ID: {} not found in course ID: {}", moduleId, courseId);
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module not found.");
    }

    moduleService.deleteModule(moduleModelOptional.get());
    log.info("Module with ID: {} successfully deleted from course ID: {}", moduleId, courseId);
    return ResponseEntity.status(HttpStatus.OK).body("Module deleted successfully.");
  }

  @PutMapping("/courses/{courseId}/modules/{moduleId}")
  public ResponseEntity<Object> updateModule(@PathVariable(value = "courseId") UUID courseId,
      @PathVariable(value = "moduleId") UUID moduleId,
      @RequestBody @Valid ModuleDto moduleDto) {
    log.info("Updating module with ID: {} in course ID: {}", moduleId, courseId);
    Optional<ModuleModel> moduleModelOptional = moduleService.findModuleIntoCourse(courseId,
        moduleId);

    if (moduleModelOptional.isEmpty()) {
      log.warn("Module with ID: {} not found in course ID: {}", moduleId, courseId);
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module not found.");
    }

    var moduleModel = moduleModelOptional.get();
    BeanUtils.copyProperties(moduleDto, moduleModel);
    moduleService.saveModule(moduleModel);
    log.info("Module with ID: {} successfully updated in course ID: {}", moduleId, courseId);
    return ResponseEntity.status(HttpStatus.OK).body(moduleModel);
  }
}