package com.example.demo.repository;

import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    User findUserById(Long userId);

    @Query("SELECT u FROM User u INNER JOIN u.addresses a WHERE u.lastName LIKE %:lastName% AND a.street LIKE %:street% order by u.id desc")
    List<User> findAllByLastNameAndStreet(String lastName, String street);
}
