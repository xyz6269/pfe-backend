package com.example.library_management.repository;


import com.example.library_management.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role ,Long> {
    Optional<Role> findByName(String Name);
}
