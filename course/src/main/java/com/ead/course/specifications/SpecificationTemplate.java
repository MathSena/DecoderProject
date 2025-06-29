package com.ead.course.specifications;

import com.ead.course.models.CourseModel;
import com.ead.course.models.CourseUserModel;
import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import java.util.Collection;
import java.util.UUID;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.jpa.domain.Specification;

public class SpecificationTemplate {

  @And({
      @Spec(path = "courseLevel", spec = Equal.class),
      @Spec(path = "courseStatus", spec = Like.class),
      @Spec(path = "name", spec = Equal.class)})
  public interface CourseSpec extends Specification<CourseModel> {

  }

  @And({
      @Spec(path = "moduleName", spec = Equal.class)})
  public interface ModuleSpec extends Specification<ModuleModel> {

  }

  @And({
      @Spec(path = "lessonName", spec = Equal.class)})
  public interface LessonSpec extends Specification<LessonModel> {

  }

  public static Specification<ModuleModel> moduleBelongsToCourse(UUID courseId) {
    return (root, query, cb) -> {
      query.distinct(true);
      Root<ModuleModel> module = root;
      Root<CourseModel> course = query.from(CourseModel.class);
      Expression<Collection<ModuleModel>> courseModules = course.get("modules");
      return cb.and(cb.equal(course.get("courseId"), courseId),
          cb.isMember(module, courseModules));

    };
  }

  public static Specification<LessonModel> lessonBelongsToModule(final UUID moduleId) {
    return (root, query, cb) -> {
      query.distinct(true);
      Root<LessonModel> lesson = root;
      Root<ModuleModel> module = query.from(ModuleModel.class);
      Expression<Collection<LessonModel>> modulesLesson = module.get("lessons");
      return cb.and(cb.equal(module.get("moduleId"), moduleId),
          cb.isMember(lesson, modulesLesson));

    };
  }

  public static Specification<CourseModel> courseUserId(final UUID userId) {
    return (root, query, cb) -> {
      assert query != null;
      query.distinct(true);
      Join<CourseModel, CourseUserModel> courseProd = root.join("courseUsers");
      return cb.equal(courseProd.get("userId"), userId);
    };
  }
}
