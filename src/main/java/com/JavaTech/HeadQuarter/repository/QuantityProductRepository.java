package com.JavaTech.HeadQuarter.repository;


import com.JavaTech.HeadQuarter.model.Branch;
import com.JavaTech.HeadQuarter.model.Product;
import com.JavaTech.HeadQuarter.model.QuantityProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuantityProductRepository extends JpaRepository<QuantityProduct, Long> {
    QuantityProduct findQuantityProductByBranchAndProduct(Branch branch, Product product);
    List<QuantityProduct> findAllByProduct(Product product);
}
