package com.ead.course.controllers;

import com.ead.course.dtos.CourseDto;
import com.ead.course.models.CourseModel;
import com.ead.course.services.CourseService;
import com.ead.course.specifications.SpecificationTemplate;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/courses")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CourseController {

  @Autowired
  CourseService courseService;


  @GetMapping
  public ResponseEntity<Page<CourseModel>> getAllCourses(SpecificationTemplate.CourseSpec spec,
      @PageableDefault(size = 10, page = 0, sort = "courseId", direction = Sort.Direction.ASC) Pageable pageable) {
    Page<CourseModel> coursePage = courseService.findAll(spec, pageable);
    return ResponseEntity.status(HttpStatus.OK).body(coursePage);
  }

  @GetMapping("/{courseId}")
  public ResponseEntity<Object> getOneCourse(@PathVariable(value = "courseId") UUID courseID) {
    Optional<CourseModel> courseModelOptional = courseService.findById(courseID);

    return courseModelOptional.<ResponseEntity<Object>>map(
            courseModel -> ResponseEntity.status(HttpStatus.OK).body(courseModel))
        .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found."));
  }

  @PostMapping
  public ResponseEntity<Object> saveCourse(@RequestBody @Valid CourseDto courseDto) {
    var courseModel = new CourseModel();
    BeanUtils.copyProperties(courseDto, courseModel);
    courseModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
    courseModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
    courseService.saveCourse(courseModel);

    return ResponseEntity.status(HttpStatus.CREATED).body(
        "Course saved successfully with ID: " + courseModel.getCourseId());

  }

  @DeleteMapping("/{courseId}")
  public ResponseEntity<Object> deleteCourse(@PathVariable(value = "courseId") UUID courseID) {
    Optional<CourseModel> courseModelOptional = courseService.findById(courseID);

    if (courseModelOptional.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found.");
    }
    courseService.deleteCourse(courseModelOptional.get());
    return ResponseEntity.status(HttpStatus.OK).body("Course deleted successfully.");
  }

  @PutMapping("/{courseId}")
  public ResponseEntity<Object> updateCourse(@PathVariable(value = "courseId") UUID courseID,
      @RequestBody @Valid CourseDto courseDto) {
    Optional<CourseModel> courseModelOptional = courseService.findById(courseID);

    if (courseModelOptional.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found.");
    }
    var courseModel = courseModelOptional.get();
    BeanUtils.copyProperties(courseDto, courseModel);
    courseModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
    return ResponseEntity.status(HttpStatus.OK).body(
        courseService.saveCourse(courseModel));

  }


}
