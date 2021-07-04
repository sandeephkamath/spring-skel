package com.cepheid.cloud.skel.repository;

import com.cepheid.cloud.skel.model.Item;
import com.cepheid.cloud.skel.model.ItemFilter;
import com.cepheid.cloud.skel.model.Item_;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@Data
public class ItemSpecification implements Specification<Item> {

    private final ItemFilter itemFilter;

    public ItemSpecification(ItemFilter itemFilter) {
        this.itemFilter = itemFilter;
    }

    @Override
    public Predicate toPredicate(Root<Item> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        Predicate namePredicate = itemFilter.getName() == null ? criteriaBuilder.conjunction() :
                criteriaBuilder.like(root.get(Item_.NAME), "%" + itemFilter.getName() + "%");
        Predicate statusPredicate = itemFilter.getStatus() == null ? criteriaBuilder.conjunction() :
                criteriaBuilder.equal(root.get(Item_.STATUS), itemFilter.getStatus());
        return criteriaBuilder.and(namePredicate, statusPredicate);

    }

}
