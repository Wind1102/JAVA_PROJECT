package com.minhhieu.identity_service.repository;

import com.minhhieu.identity_service.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;


@Repository
public interface PermissionRepository extends JpaRepository<Permission,String> {
//    Set<Permission> findAllById(Set<Permission> permissions);
}
