package com.udacity.jdnd.course3.critter.repository;

import com.udacity.jdnd.course3.critter.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface UserRepository  extends JpaRepository<User, Long> {
}
