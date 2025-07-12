package com.ead.course.services.impl;

import com.ead.course.services.UtilsService;
import java.util.UUID;
import org.springframework.stereotype.Service;


@Service
public class UtilsServiceImpl implements UtilsService {

  @Override
  public String createUrlGetAllUsersByCourse(UUID courseId,
      org.springframework.data.domain.Pageable pageable) {
    return "/users?courseId=" + courseId + "&page=" + pageable.getPageNumber() + "&size="
        + pageable.getPageSize() + "&sort=" + pageable.getSort().toString().replaceAll(": ", ",");
  }

}
