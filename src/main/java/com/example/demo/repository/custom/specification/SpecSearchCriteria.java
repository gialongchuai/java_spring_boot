package com.example.demo.repository.custom.specification;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static com.example.demo.repository.custom.specification.SearchOperation.*;

@Getter
@Setter
@NoArgsConstructor
public class SpecSearchCriteria {
    // firstName:*John*

    private String key; // firstName
    private SearchOperation operation; // : EQUALITY ~ LIKE
    private Object value; // John
    private boolean orPredicate; // ' : OR

    public SpecSearchCriteria(String key, SearchOperation operation, Object value) {
        super();
        this.key = key;
        this.operation = operation;
        this.value = value;
    }

    public SpecSearchCriteria(String orPredicate, String key, SearchOperation operation, Object value) {
        super();
        this.orPredicate = orPredicate != null && orPredicate.equals(OR_PREDICATE_FLAG);
        this.key = key;
        this.operation = operation;
        this.value = value;
    }

    public SpecSearchCriteria(String key, String operation, String value, String prefix, String suffix) {
        SearchOperation oper = SearchOperation.getSimpleOperation(operation.charAt(0));
        if(oper != null) {
            if(oper == EQUALITY) {
                boolean startWithAsterisk = prefix != null && prefix.contains(ZERO_OR_MORE_REGEX);
                boolean endWithAsterisk = suffix != null && suffix.contains(ZERO_OR_MORE_REGEX);

                if(startWithAsterisk && endWithAsterisk) {
                    oper = CONTAINS;
                } else if(startWithAsterisk) {
                    oper = ENDS_WITH;
                } else if (endWithAsterisk) {
                    oper = STARTS_WITH;
                }
            }
        }
        this.key = key;
        this.operation = oper;
        this.value = value;
    }
}
