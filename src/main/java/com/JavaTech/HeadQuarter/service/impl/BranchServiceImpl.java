package com.JavaTech.HeadQuarter.service.impl;

import com.JavaTech.HeadQuarter.dto.BranchDTO;
import com.JavaTech.HeadQuarter.model.Branch;
import com.JavaTech.HeadQuarter.model.QuantityProduct;
import com.JavaTech.HeadQuarter.repository.BranchRepository;
import com.JavaTech.HeadQuarter.service.BranchService;
import com.JavaTech.HeadQuarter.service.ProductService;
import com.JavaTech.HeadQuarter.service.QuantityProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BranchServiceImpl implements BranchService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private QuantityProductService quantityProductService;

    @Override
    public List<Branch> listAll() {
        return branchRepository.findAll();
    }

    @Override
    public Branch saveOrUpdate(Branch branch) {
        Branch branch_Saved = branchRepository.save(branch);
        productService.listAll().forEach(product -> {
            quantityProductService.saveOrUpdate(QuantityProduct.builder()
                    .product(product)
                    .branch(branch_Saved)
                    .quantity(0).build());
        });

        return branch_Saved;
    }

    @Override
    public Branch findByName(String name) {
        return branchRepository.findBranchByName(name).orElse(null);
    }

    @Override
    public List<BranchDTO> listAllDTO() {
        return listAll().stream()
                .map(branch -> modelMapper.map(branch, BranchDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public Branch findByid(Long id) {
        return branchRepository.findById(id).orElse(null);
    }
}
