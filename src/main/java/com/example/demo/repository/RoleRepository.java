package com.example.demo.repository;

import com.example.demo.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    @Query(value = "select r from Role r inner join UserHasRole uhr on r.id = uhr.role.id where uhr.user.id = :userId")
    List<Role> getAllByUserId(Long userId);
}
