package com.JavaTech.HeadQuarter.service;

import com.JavaTech.HeadQuarter.dto.BranchDTO;
import com.JavaTech.HeadQuarter.model.Branch;

import java.util.List;

public interface BranchService {

    List<Branch> listAll();
    Branch saveOrUpdate(Branch branch);

    Branch findByName(String name);

    List<BranchDTO> listAllDTO();

    Branch findByid(Long id);
}
