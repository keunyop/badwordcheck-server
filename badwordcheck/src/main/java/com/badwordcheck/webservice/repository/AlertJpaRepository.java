package com.badwordcheck.webservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.badwordcheck.webservice.dto.Alert;

@Repository
public interface AlertJpaRepository extends JpaRepository<Alert, Long> {
    List<Alert> findByUsername(String username);
}
