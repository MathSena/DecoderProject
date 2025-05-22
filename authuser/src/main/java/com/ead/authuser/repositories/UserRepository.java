package com.ead.authuser.repositories;

import com.ead.authuser.models.UserModel;
import com.ead.authuser.specifications.SpecificationTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserModel, UUID>,
    JpaSpecificationExecutor<UserModel> {

  Optional<Object> findByUsername(String username);

  Optional<Object> findByEmail(String email);

  Page<UserModel> findAll(Pageable pageable, SpecificationTemplate.UserSpec spec);
}
