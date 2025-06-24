package com.ead.authuser.repositories;

import com.ead.authuser.models.UserCourseModel;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCourseRepository extends JpaRepository<UserCourseModel, UUID> {

}
