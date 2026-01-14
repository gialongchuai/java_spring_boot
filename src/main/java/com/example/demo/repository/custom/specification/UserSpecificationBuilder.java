package com.example.demo.repository.custom.specification;

import com.example.demo.model.User;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

import static com.example.demo.repository.custom.specification.SearchOperation.*;

public class UserSpecificationBuilder {

    public final List<SpecSearchCriteria> params;

    public UserSpecificationBuilder() {
        this.params = new ArrayList<>();
    }

    public UserSpecificationBuilder with(String key, String operation, Object value, String prefix, String suffix) {
        return with(null, key, operation, value, prefix, suffix);
    }

    public UserSpecificationBuilder with(String orPredicate, String key, String operation, Object value, String prefix, String suffix) {
        SearchOperation oper = SearchOperation.getSimpleOperation(operation.charAt(0));
        if (oper != null) {
            if (oper == EQUALITY) {
                boolean startWithAsterisk = prefix != null && prefix.contains(ZERO_OR_MORE_REGEX);
                boolean endWithAsterisk = suffix != null && suffix.contains(ZERO_OR_MORE_REGEX);

                if (startWithAsterisk && endWithAsterisk) {
                    oper = CONTAINS;
                } else if (startWithAsterisk) {
                    oper = ENDS_WITH;
                } else if (endWithAsterisk) {
                    oper = STARTS_WITH;
                }
            }
            params.add(new SpecSearchCriteria(orPredicate, key, oper, value));
        }
        return this;
    }

    public Specification<User> build() {
        if (params.isEmpty()) return null;

        Specification<User> specification = new UserSpecification(params.get(0)); // bên ông kia phải có AllArge không ấy quăng lỗi chỗ này !!!
        int i = 0; // lấy từ 1 vì 0 dòng trên get rồi
        for (SpecSearchCriteria criteria : params) {
            if (i != 0) {
                specification = params.get(i).isOrPredicate()
                        ? Specification.where(specification).or(new UserSpecification(params.get(i)))
                        : Specification.where(specification).and(new UserSpecification(params.get(i)));
            }
            i++;
        }
        return specification;
    }
}
