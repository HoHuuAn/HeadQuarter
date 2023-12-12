package com.JavaTech.HeadQuarter.service.impl;

import com.JavaTech.HeadQuarter.model.Branch;
import com.JavaTech.HeadQuarter.model.Product;
import com.JavaTech.HeadQuarter.model.QuantityProduct;
import com.JavaTech.HeadQuarter.repository.QuantityProductRepository;
import com.JavaTech.HeadQuarter.service.QuantityProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuantityProductServiceImpl implements QuantityProductService {

    @Autowired
    private QuantityProductRepository quantityProductRepository;

    @Override
    public QuantityProduct saveOrUpdate(QuantityProduct quantityProduct) {
        return quantityProductRepository.save(quantityProduct);
    }

    @Override
    public QuantityProduct findByBranchAndProduct(Branch branch, Product product) {
        return quantityProductRepository.findQuantityProductByBranchAndProduct(branch, product);
    }

    @Override
    public List<QuantityProduct> findAllByProduct(Product product) {
        return quantityProductRepository.findAllByProduct(product);
    }
}
