package com.ead.course.services.impl;

import com.ead.course.clients.AuthUserClient;
import com.ead.course.models.CourseModel;
import com.ead.course.models.CourseUserModel;
import com.ead.course.repositories.CourseUserRepository;
import com.ead.course.services.CourseUserService;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CourseUserServiceImpl implements CourseUserService {

  @Autowired
  private CourseUserRepository courseUserRepository;

  @Autowired
  AuthUserClient authUserClient;

  @Override
  public boolean existsByCourseAndUserId(CourseModel courseModel, UUID userId) {
    return courseUserRepository.existsByCourseAndUserId(courseModel, userId);
  }

  @Override
  public CourseUserModel save(CourseUserModel courseUserModel) {
    return courseUserRepository.save(courseUserModel);
  }

  @Transactional
  @Override
  public CourseUserModel saveAndSendSubscriptionUserInCourse(CourseUserModel courseUserModel) {
    courseUserModel = courseUserRepository.save(courseUserModel);
    authUserClient.postSubscriptionUserInCourse(courseUserModel.getCourse().getCourseId(),
        courseUserModel.getUserId());
    return courseUserModel;
  }

  @Override
  public boolean existsByUserId(UUID userId) {
    return courseUserRepository.existsByUserId(userId);
  }

  @Transactional
  @Override
  public void deleteCourseUserByUser(UUID userId) {
    courseUserRepository.deleteAllByUserId(userId);
  }

}
