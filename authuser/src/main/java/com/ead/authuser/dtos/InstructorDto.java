package com.ead.authuser.dtos;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Data;

@Data
public class InstructorDto {

    @NotNull
    private UUID userId;
}
