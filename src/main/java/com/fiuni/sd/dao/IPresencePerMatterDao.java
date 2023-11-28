package com.fiuni.sd.dao;

import com.fiuni.sd.domain.PresencePerMatterDomain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPresencePerMatterDao extends JpaRepository<PresencePerMatterDomain, Integer> {

    public Page<PresencePerMatterDomain> findAll(Pageable pageable);

}