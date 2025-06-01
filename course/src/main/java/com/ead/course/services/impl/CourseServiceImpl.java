package com.ead.course.services.impl;

import com.ead.course.models.CourseModel;
import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.repositories.CourseRepository;
import com.ead.course.repositories.LessonRepository;
import com.ead.course.repositories.ModuleRepository;
import com.ead.course.services.CourseService;
import com.ead.course.specifications.SpecificationTemplate.CourseSpec;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class CourseServiceImpl implements CourseService {

  @Autowired
  CourseRepository courseRepository;
  @Autowired
  ModuleRepository moduleRepository;
  @Autowired
  LessonRepository lessonRepository;

  @Transactional
  @Override
  public void deleteCourse(CourseModel courseModel) {
    log.info("Deleting course with ID: {}", courseModel.getCourseId());
    List<ModuleModel> moduleModels = moduleRepository.findAllModulesIntoCourse(
        courseModel.getCourseId());
    if (!moduleModels.isEmpty()) {
      for (ModuleModel moduleModel : moduleModels) {
        log.info("Deleting lessons for module ID: {}", moduleModel.getModuleId());
        List<LessonModel> lessonModels = lessonRepository.findAllLessonsIntoModule(
            moduleModel.getModuleId());
        if (!lessonModels.isEmpty()) {
          lessonRepository.deleteAll(lessonModels);
          log.info("Deleted {} lessons for module ID: {}", lessonModels.size(),
              moduleModel.getModuleId());
        }
      }
      moduleRepository.deleteAll(moduleModels);
      log.info("Deleted {} modules for course ID: {}", moduleModels.size(),
          courseModel.getCourseId());
    }
    courseRepository.delete(courseModel);
    log.info("Course with ID: {} successfully deleted.", courseModel.getCourseId());
  }

  @Override
  public CourseModel saveCourse(CourseModel courseModel) {
    log.info("Saving course with ID: {}", courseModel.getCourseId());
    CourseModel savedCourse = courseRepository.save(courseModel);
    log.info("Course with ID: {} successfully saved.", savedCourse.getCourseId());
    return savedCourse;
  }

  @Override
  public Optional<CourseModel> findById(UUID courseID) {
    log.info("Fetching course with ID: {}", courseID);
    Optional<CourseModel> course = courseRepository.findById(courseID);
    if (course.isPresent()) {
      log.info("Course with ID: {} found.", courseID);
    } else {
      log.warn("Course with ID: {} not found.", courseID);
    }
    return course;
  }

  @Override
  public Page<CourseModel> findAll(CourseSpec spec, Pageable pageable) {
    log.info("Fetching all courses with specification and pagination.");
    Page<CourseModel> courses = courseRepository.findAll(spec, pageable);
    log.info("Successfully fetched {} courses.", courses.getTotalElements());
    return courses;
  }
}