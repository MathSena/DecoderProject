package com.ead.course.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "tb_modules")
public class ModuleModel implements Serializable {

  private static final long serialVersionUID = 1l;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID moduleId;
  @Column(nullable = false, unique = true, length = 150)
  private String moduleName;
  @Column(nullable = false, unique = true, length = 150)
  private String moduleDescription;
  @Column(nullable = false)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
  private LocalDateTime creationDate;

}
