package com.example.demo.repository;

import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    Optional<User> findByUsername(String username);

    @Query(value = "select u from User u where u.username like :username")
    User findByUsernameEntity(String username);

    User findByEmail(String email);

    @Query("SELECT u FROM User u INNER JOIN u.addresses a WHERE u.lastName LIKE %:lastName% AND a.street LIKE %:street% order by u.id desc")
    List<User> findAllByLastNameAndStreet(String lastName, String street);

    boolean existsByUsername(String username);
}
