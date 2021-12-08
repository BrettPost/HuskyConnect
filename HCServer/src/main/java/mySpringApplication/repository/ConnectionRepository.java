package mySpringApplication.repository;

import mySpringApplication.model.Connection;
import mySpringApplication.model.ConnectionId;
import mySpringApplication.model.UserTag;
import mySpringApplication.model.UserTagId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConnectionRepository extends JpaRepository<Connection, ConnectionId>{ }