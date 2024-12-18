package com.minhhieu.identity_service.repository;

import com.minhhieu.identity_service.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users,String> {
        public boolean existsByUsername(String username);
        Optional<Users> findByUsername(String username);
}
