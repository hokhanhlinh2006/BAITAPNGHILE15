package com.example.employeemanagement.specification;

import com.example.employeemanagement.entity.Employee;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

public class EmployeeSpecification {

    public static Specification<Employee> filter(
            String name,
            Long departmentId,
            Integer minAge,
            Integer maxAge
    ) {
        return (Root<Employee> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {

            Predicate predicate = cb.conjunction();

            if (name != null && !name.isEmpty()) {
                predicate = cb.and(predicate,
                        cb.like(cb.lower(root.get("name")),
                                "%" + name.toLowerCase() + "%"));
            }

            if (departmentId != null) {
                predicate = cb.and(predicate,
                        cb.equal(root.get("department").get("id"), departmentId));
            }

            if (minAge != null) {
                predicate = cb.and(predicate,
                        cb.greaterThanOrEqualTo(root.get("age"), minAge));
            }

            if (maxAge != null) {
                predicate = cb.and(predicate,
                        cb.lessThanOrEqualTo(root.get("age"), maxAge));
            }

            return predicate;
        };
    }
}