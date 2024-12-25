package com.minhhieu.identity_service.repository;

import com.minhhieu.identity_service.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;



public interface PermissionRepository extends JpaRepository<Permission,String> {

}
