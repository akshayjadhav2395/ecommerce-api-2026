package com.akshay.ecommerce.product.specification;

import com.akshay.ecommerce.product.entity.Product;
import com.akshay.ecommerce.product.entity.ProductStatus;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.parameters.P;

public class ProductSpecification {

    public static Specification<Product> hasBrand(String brand) {

        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(
                criteriaBuilder.lower(root.get("brand")),
                brand.toLowerCase()));
    }

    public static Specification<Product> hasStatus(ProductStatus status) {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("status"), status);
    }

    public static Specification<Product> hasKeyword(String keyword) {

        return (root, query, criteriaBuilder) -> {

            String search = "%" + keyword.toLowerCase() + "%";

            return criteriaBuilder.or(
                    criteriaBuilder.like(
                            criteriaBuilder.lower(root.get("name")),
                            search),
                    criteriaBuilder.like(
                            criteriaBuilder.lower(root.get("brand")),
                            search)
                    );

        };

    }
}