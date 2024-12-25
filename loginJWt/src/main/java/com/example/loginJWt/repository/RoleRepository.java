package com.example.loginJWt.repository;

import com.example.loginJWt.enitity.ERole;
import com.example.loginJWt.enitity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> finbyName(ERole name);
}
