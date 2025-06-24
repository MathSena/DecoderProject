package com.ead.course.repositories;

import com.ead.course.models.CourseUserModel;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseUserRepository extends JpaRepository<CourseUserModel, UUID> {

}
