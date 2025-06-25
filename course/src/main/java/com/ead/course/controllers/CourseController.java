package com.ead.course.controllers;

import com.ead.course.dtos.CourseDto;
import com.ead.course.models.CourseModel;
import com.ead.course.services.CourseService;
import com.ead.course.specifications.SpecificationTemplate;
import com.ead.course.specifications.SpecificationTemplate.CourseSpec;
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
@RequestMapping("/courses")
@CrossOrigin(origins = "*", maxAge = 3600)
@Slf4j
public class CourseController {

  @Autowired
  CourseService courseService;

  @GetMapping
  public ResponseEntity<Page<CourseModel>> getAllCourses(SpecificationTemplate.CourseSpec spec,
      @PageableDefault(size = 10, page = 0, sort = "courseId", direction = Sort.Direction.ASC) Pageable pageable,
      @RequestParam(required = false) UUID userId) {
    if (userId != null) {
      return ResponseEntity.status(HttpStatus.OK)
          .body(courseService.findAll(
              (CourseSpec) SpecificationTemplate.courseUserId(userId).and(spec), pageable));

    } else {
      log.info("Fetching all courses with pagination and specification.");
      Page<CourseModel> coursePage = courseService.findAll(spec, pageable);
      log.info("Successfully fetched {} courses.", coursePage.getTotalElements());
      return ResponseEntity.status(HttpStatus.OK).body(coursePage);
    }

  }

  @GetMapping("/{courseId}")
  public ResponseEntity<Object> getOneCourse(@PathVariable(value = "courseId") UUID courseID) {
    log.info("Fetching course with ID: {}", courseID);
    Optional<CourseModel> courseModelOptional = courseService.findById(courseID);

    if (courseModelOptional.isPresent()) {
      log.info("Course with ID: {} found.", courseID);
      return ResponseEntity.status(HttpStatus.OK).body(courseModelOptional.get());
    } else {
      log.warn("Course with ID: {} not found.", courseID);
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found.");
    }
  }

  @PostMapping
  public ResponseEntity<Object> saveCourse(@RequestBody @Valid CourseDto courseDto) {
    log.info("Saving a new course.");
    var courseModel = new CourseModel();
    BeanUtils.copyProperties(courseDto, courseModel);
    courseModel.setUserInstructor(courseDto.getCourseInstructorId());
    courseModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
    courseModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
    courseService.saveCourse(courseModel);
    log.info("Course successfully saved with ID: {}", courseModel.getCourseId());
    return ResponseEntity.status(HttpStatus.CREATED).body(
        "Course saved successfully with ID: " + courseModel.getCourseId());
  }

  @DeleteMapping("/{courseId}")
  public ResponseEntity<Object> deleteCourse(@PathVariable(value = "courseId") UUID courseID) {
    log.info("Deleting course with ID: {}", courseID);
    Optional<CourseModel> courseModelOptional = courseService.findById(courseID);

    if (courseModelOptional.isEmpty()) {
      log.warn("Course with ID: {} not found.", courseID);
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found.");
    }
    courseService.deleteCourse(courseModelOptional.get());
    log.info("Course with ID: {} successfully deleted.", courseID);
    return ResponseEntity.status(HttpStatus.OK).body("Course deleted successfully.");
  }

  @PutMapping("/{courseId}")
  public ResponseEntity<Object> updateCourse(@PathVariable(value = "courseId") UUID courseID,
      @RequestBody @Valid CourseDto courseDto) {
    log.info("Updating course with ID: {}", courseID);
    Optional<CourseModel> courseModelOptional = courseService.findById(courseID);

    if (courseModelOptional.isEmpty()) {
      log.warn("Course with ID: {} not found.", courseID);
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found.");
    }
    var courseModel = courseModelOptional.get();
    BeanUtils.copyProperties(courseDto, courseModel);
    courseModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
    courseService.saveCourse(courseModel);
    log.info("Course with ID: {} successfully updated.", courseID);
    return ResponseEntity.status(HttpStatus.OK).body(courseModel);
  }
}