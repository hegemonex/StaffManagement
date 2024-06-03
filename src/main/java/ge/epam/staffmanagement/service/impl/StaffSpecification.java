package ge.epam.staffmanagement.service.impl;

import ge.epam.staffmanagement.entity.Staff;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

@NoArgsConstructor
@AllArgsConstructor
public class StaffSpecification implements Specification<Staff> {
    private String query;

    @Override
    public Specification<Staff> and(Specification<Staff> other) {
        return Specification.super.and(other);
    }

    @Override
    public Specification<Staff> or(Specification<Staff> other) {
        return Specification.super.or(other);
    }

    @Override
    public Predicate toPredicate(Root<Staff> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        if (query == null || query.isEmpty()) {
            return criteriaBuilder.conjunction();
        }
        //FIXME: don't use raw query, you should apply some 'SearchStuffDto' and work with that. Pls google it
        String likePattern = "%" + query.toLowerCase() + "%";
        return criteriaBuilder.or(
                criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")), likePattern),
                criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName")), likePattern),
                criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), likePattern),
                criteriaBuilder.like(criteriaBuilder.lower(root.get("contactNumber")), likePattern),
                criteriaBuilder.like(criteriaBuilder.lower(root.join("department").get("name")), likePattern)
        );
    }
}

