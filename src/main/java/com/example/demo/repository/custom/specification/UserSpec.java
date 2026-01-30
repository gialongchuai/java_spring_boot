package com.example.demo.repository.custom.specification;

import com.example.demo.model.User;
import com.example.demo.util.Gender;
import org.springframework.data.jpa.domain.Specification;

public class UserSpec {
    public static Specification<User> hasFirstName(String firstName) {
        return (root, query, criteriaBuilder)
                -> criteriaBuilder.like(root.get("firstName"), "%" + firstName + "%");
    }

    public static Specification<User> notEqualGender(Gender gender) {
        return (root, query, criteriaBuilder)
                -> criteriaBuilder.notEqual(root.get("gender"), gender);
    }
}
