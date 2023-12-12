package com.JavaTech.HeadQuarter.service;

import com.JavaTech.HeadQuarter.model.Branch;
import com.JavaTech.HeadQuarter.model.Product;
import com.JavaTech.HeadQuarter.model.QuantityProduct;

import java.util.List;

public interface QuantityProductService {
    QuantityProduct saveOrUpdate(QuantityProduct quantityProduct);
    QuantityProduct findByBranchAndProduct(Branch branch, Product product);
    List<QuantityProduct> findAllByProduct(Product product);
}
