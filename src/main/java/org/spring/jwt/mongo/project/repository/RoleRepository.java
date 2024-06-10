package org.spring.jwt.mongo.project.repository;

import org.spring.jwt.mongo.project.models.ERole;
import org.spring.jwt.mongo.project.models.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RoleRepository extends MongoRepository<Role, String> {

    Optional<Role> findByName(ERole role);
}
