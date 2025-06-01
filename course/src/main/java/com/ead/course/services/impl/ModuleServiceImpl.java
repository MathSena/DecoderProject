package com.ead.course.services.impl;

import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.repositories.LessonRepository;
import com.ead.course.repositories.ModuleRepository;
import com.ead.course.services.ModuleService;
import jakarta.transaction.Transactional;
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
public class ModuleServiceImpl implements ModuleService {

  @Autowired
  ModuleRepository moduleRepository;
  @Autowired
  LessonRepository lessonRepository;

  @Transactional
  @Override
  public void deleteModule(ModuleModel moduleModel) {
    log.info("Deleting module with ID: {}", moduleModel.getModuleId());
    List<LessonModel> lessonModels = lessonRepository.findAllLessonsIntoModule(moduleModel.getModuleId());

    if (!lessonModels.isEmpty()) {
      log.info("Deleting {} lessons for module ID: {}", lessonModels.size(), moduleModel.getModuleId());
      lessonRepository.deleteAll(lessonModels);
    }
    moduleRepository.delete(moduleModel);
    log.info("Module with ID: {} successfully deleted.", moduleModel.getModuleId());
  }

  @Override
  public ModuleModel saveModule(ModuleModel moduleModel) {
    log.info("Saving module with ID: {}", moduleModel.getModuleId());
    ModuleModel savedModule = moduleRepository.save(moduleModel);
    log.info("Module with ID: {} successfully saved.", savedModule.getModuleId());
    return savedModule;
  }

  @Override
  public Optional<ModuleModel> findModuleIntoCourse(UUID courseId, UUID moduleId) {
    log.info("Fetching module with ID: {} in course ID: {}", moduleId, courseId);
    Optional<ModuleModel> module = moduleRepository.findModuleIntoCourse(courseId, moduleId);
    if (module.isPresent()) {
      log.info("Module with ID: {} found in course ID: {}", moduleId, courseId);
    } else {
      log.warn("Module with ID: {} not found in course ID: {}", moduleId, courseId);
    }
    return module;
  }

  @Override
  public List<ModuleModel> findAllByCourse(UUID courseId) {
    log.info("Fetching all modules for course ID: {}", courseId);
    List<ModuleModel> modules = moduleRepository.findAllModulesIntoCourse(courseId);
    log.info("Successfully fetched {} modules for course ID: {}", modules.size(), courseId);
    return modules;
  }

  @Override
  public Optional<ModuleModel> findById(UUID moduleId) {
    log.info("Fetching module with ID: {}", moduleId);
    Optional<ModuleModel> module = moduleRepository.findById(moduleId);
    if (module.isPresent()) {
      log.info("Module with ID: {} found.", moduleId);
    } else {
      log.warn("Module with ID: {} not found.", moduleId);
    }
    return module;
  }

  @Override
  public Page<ModuleModel> findAllByCourse(Specification<ModuleModel> spec, Pageable pageable) {
    log.info("Fetching all modules with specification and pagination.");
    Page<ModuleModel> modules = moduleRepository.findAll(spec, pageable);
    log.info("Successfully fetched {} modules.", modules.getTotalElements());
    return modules;
  }
}