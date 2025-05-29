package com.ead.course.repositories;

import com.ead.course.models.CourseModel;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CourseRepository extends JpaRepository<CourseModel, UUID>,
    JpaSpecificationExecutor<CourseModel> {


}
