package com.example.demo.repository.custom.criteria;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.function.Consumer;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserSearchCriteriaQueryConsumer implements Consumer<SearchCriteria> {

//    SearchCriteria: key, operation, value
//    SearchCriteria: firstName, :><, value

    private CriteriaBuilder criteriaBuilder;
    private Predicate predicate; // nhớ import đúng
    private Root root;

    @Override
    public void accept(SearchCriteria params) {
        if(params.getOperation().equals(">")) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.greaterThanOrEqualTo(root.get(params.getKey()), params.getValue().toString()));
        } else if(params.getOperation().equals("<")) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.lessThanOrEqualTo(root.get(params.getKey()), params.getValue().toString()));
        } else { // nếu String thì like, còn nếu khác thì equals
            if(root.get(params.getKey()).getJavaType() == String.class) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get(params.getKey()), "%" + params.getValue().toString() + "%"));
            } else {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get(params.getKey()), params.getValue().toString()));
            }
        }
    }
}
