package com.fiuni.sd.dao;

import com.fiuni.sd.domain.Role;

import org.springframework.stereotype.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface IRoleDao extends JpaRepository<Role, Integer> {

    Optional<Role> findByName(String name);

}
