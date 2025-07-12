package com.ead.course.dtos;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Data;

@Data
public class SubscriptionDto {

  @NotNull
  private UUID userId;
}
