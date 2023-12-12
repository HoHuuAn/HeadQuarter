package com.JavaTech.HeadQuarter.repository;

import com.JavaTech.HeadQuarter.model.ERole;
import com.JavaTech.HeadQuarter.model.Role;
import jdk.jfr.Registered;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
