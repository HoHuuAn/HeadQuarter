package com.JavaTech.HeadQuarter.service.impl;

import com.JavaTech.HeadQuarter.dto.BranchDTO;
import com.JavaTech.HeadQuarter.model.Branch;
import com.JavaTech.HeadQuarter.repository.BranchRepository;
import com.JavaTech.HeadQuarter.service.BranchService;
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

    @Override
    public List<Branch> listAll() {
        return branchRepository.findAll();
    }

    @Override
    public Branch saveOrUpdate(Branch branch) {
        return branchRepository.save(branch);
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
}
