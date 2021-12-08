package mySpringApplication.repository;

import mySpringApplication.model.UserTag;
import mySpringApplication.model.UserTagId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTagRepository extends JpaRepository<UserTag, UserTagId>{ }