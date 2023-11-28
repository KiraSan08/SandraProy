package com.fiuni.sd.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fiuni.sd.domain.User;

import java.util.Optional;

@Repository
public interface IUserDao extends JpaRepository<User, Integer> {
	Optional<User> findByEmail(String email);
}
