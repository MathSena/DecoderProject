package com.ead.course.repositories;

import com.ead.course.models.ModuleModel;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ModuleRepository extends JpaRepository<ModuleModel, UUID>,
    JpaSpecificationExecutor<ModuleModel> {

  @Query(value = "SELECT m FROM tb_modules m WHERE m.course.courseId = :courseId", nativeQuery = true)
  List<ModuleModel> findAllModulesIntoCourse(@Param("courseId") UUID courseId);


  @Query(value = "SELECT m FROM tb_modules m WHERE m.course.courseId = :courseId AND m.moduleId = :moduleId", nativeQuery = true)
  Optional<ModuleModel> findModuleIntoCourse(@Param("courseId") UUID courseId,
      @Param("moduleId") UUID moduleId);
}
