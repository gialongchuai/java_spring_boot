package com.example.demo.repository.custom.specification;

import com.example.demo.model.User;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jspecify.annotations.Nullable;
import org.springframework.data.jpa.domain.Specification;

@Getter
@AllArgsConstructor
public class UserSpecification implements Specification<User> {

    private SpecSearchCriteria searchCriteria;

    @Override
    public @Nullable Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        return switch (searchCriteria.getOperation()) {
            case EQUALITY
                    -> criteriaBuilder.equal(root.get(searchCriteria.getKey()), searchCriteria.getValue());
            case NEGATION
                    -> criteriaBuilder.notEqual(root.get(searchCriteria.getKey()), searchCriteria.getValue());
            case GREATER_THAN
                    -> criteriaBuilder.greaterThan(root.get(searchCriteria.getKey()), searchCriteria.getValue().toString());
            case LESS_THAN
                    -> criteriaBuilder.lessThan(root.get(searchCriteria.getKey()), searchCriteria.getValue().toString());
            case LIKE
                    -> criteriaBuilder.like(root.get(searchCriteria.getKey()), "%" + searchCriteria.getValue().toString() + "%");
            case STARTS_WITH
                    -> criteriaBuilder.like(root.get(searchCriteria.getKey()), searchCriteria.getValue().toString() + "%");
            case ENDS_WITH
                    -> criteriaBuilder.like(root.get(searchCriteria.getKey()), "%" + searchCriteria.getValue());
            case CONTAINS
                    -> criteriaBuilder.like(root.get(searchCriteria.getKey()), "%" + searchCriteria.getValue() + "%");
        };
    }
}
