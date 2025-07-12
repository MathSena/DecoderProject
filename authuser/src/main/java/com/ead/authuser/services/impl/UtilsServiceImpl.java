package com.ead.authuser.services.impl;

import com.ead.authuser.services.UtilsService;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UtilsServiceImpl implements UtilsService {


  public String createUrlGetAllCoursesByUser(UUID userId, Pageable pageable) {
    return "/courses?userId=" + userId + "&page=" + pageable.getPageNumber() + "&size="
        + pageable.getPageSize() + "&sort=" + pageable.getSort().toString().replaceAll(": ", ",");
  }
}
