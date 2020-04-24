package com.duytran.kdtrace.repository;

import com.duytran.kdtrace.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findUserById(long id);
    boolean existsUserByUsername(String username);
    List<User> findAll();
}
