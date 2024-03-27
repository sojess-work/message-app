package com.org.messageapp.repository;

import com.org.messageapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User,String> {

    Optional<User> findById(String id);

    Optional<User> findByEmail(String email);
}
