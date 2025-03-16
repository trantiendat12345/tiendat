package com.example.tiendat.specifications;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;

public class BaseSpecification<T> {

    private static final Logger logger = LoggerFactory.getLogger(BaseSpecification.class);

    public static <T> Specification<T> keywordSpec(String keyword, String... fields) {

        return (root, query, criteriaBuilder) -> {

            if(keyword == null || keyword.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            Predicate[] predicates = new Predicate[fields.length];
            for(int i = 0; i < fields.length; i++) {
                predicates[i] = criteriaBuilder.like(
                    criteriaBuilder.lower(root.get(fields[i])), "%" + keyword.toLowerCase() + "%"
                );
            }

            return criteriaBuilder.or(predicates);

        };

    }

    public static <T> Specification<T> whereSpec(Map<String, String> filters) {

        return (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = filters.entrySet().stream().map(entry -> criteriaBuilder.equal(root.get(entry.getKey()), entry.getValue())).collect(Collectors.toList());

            logger.info("predicates: {}", predicates);

            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));

        };

    }

    public static <T> Specification<T> complexWhereSpec(Map<String, Map<String, String>> filters) {

        return (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = filters.entrySet().stream().flatMap((entry) -> entry.getValue().entrySet().stream().map(condition -> {
                
                String field = entry.getKey();
                String operator = condition.getKey();
                String value = condition.getValue();

                switch (operator.toLowerCase()) {
                    case "eq":
                        return criteriaBuilder.equal(root.get(field), value);
                    case "gt":
                        return criteriaBuilder.greaterThan(root.get(field), value);
                    case "gte":
                        return criteriaBuilder.greaterThanOrEqualTo(root.get(field), value);
                    case "lt":
                        return criteriaBuilder.lessThan(root.get(field), value);
                    case "lte":
                        return criteriaBuilder.lessThanOrEqualTo(root.get(field), value);
                    case "in":
                        List<String> values = List.of(value.split(","));

                        return root.get(field).in(values);
                    default:
                        throw new IllegalArgumentException("Toan tu " + operator + " khong hop le");
                }

            })).collect(Collectors.toList());

            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));

        };

    }
    
}
