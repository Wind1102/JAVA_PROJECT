package com.minhhieu.identity_service.repository;

import com.minhhieu.identity_service.entity.InvalidatedToken;
import com.minhhieu.identity_service.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface InvalidateTokenRepository extends JpaRepository<InvalidatedToken,String> {
}
