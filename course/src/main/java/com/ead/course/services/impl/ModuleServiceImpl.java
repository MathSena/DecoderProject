package com.ead.course.services.impl;

import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.repositories.LessonRepository;
import com.ead.course.repositories.ModuleRepository;
import com.ead.course.services.ModuleService;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ModuleServiceImpl implements ModuleService {

  @Autowired
  ModuleRepository moduleRepository;
  @Autowired
  LessonRepository lessonRepository;


  @Transactional
  @Override
  public void deleteModule(ModuleModel moduleModel) {
    List<LessonModel> lessonModels = lessonRepository.findAllLessonsIntoModule(
        moduleModel.getModuleId());

    if (!lessonModels.isEmpty()) {
      lessonRepository.deleteAll(lessonModels);
    }
    moduleRepository.delete(moduleModel);

  }

  @Override
  public ModuleModel saveModule(ModuleModel moduleModel) {
    return moduleRepository.save(moduleModel);
  }

  @Override
  public Optional<ModuleModel> findModuleIntoCourse(UUID courseId, UUID moduleId) {
    return moduleRepository.findModuleIntoCourse(courseId, moduleId);
  }

  @Override
  public List<ModuleModel> findAllByCourse(UUID courseId) {
    return moduleRepository.findAllModulesIntoCourse(courseId);
  }
}
