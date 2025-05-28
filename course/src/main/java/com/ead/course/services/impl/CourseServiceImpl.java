package com.ead.course.services.impl;

import com.ead.course.models.CourseModel;
import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.repositories.CourseRepository;
import com.ead.course.repositories.LessonRepository;
import com.ead.course.repositories.ModuleRepository;
import com.ead.course.services.CourseService;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
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
    List<ModuleModel> moduleModels = moduleRepository.findAllModulesIntoCourse(
        courseModel.getCourseId());
    if (!moduleModels.isEmpty()) {
      for (ModuleModel moduleModel : moduleModels) {
        List<LessonModel> lessonModels = lessonRepository.findAllLessonsIntoModule(
            moduleModel.getModuleId());
        if (!lessonModels.isEmpty()) {
          lessonRepository.deleteAll(lessonModels);
        }
      }
      moduleRepository.deleteAll(moduleModels);
    }
    courseRepository.delete(courseModel);
  }

  @Override
  public CourseModel saveCourse(CourseModel courseModel) {
    return courseRepository.save(courseModel);
  }

  @Override
  public Optional<CourseModel> findById(UUID courseID) {
    return courseRepository.findById(courseID);
  }

  @Override
  public List<CourseModel> findAll() {
    return courseRepository.findAll();
  }
}
