package com.JavaTech.HeadQuarter.controller;


import com.JavaTech.HeadQuarter.dto.BranchDTO;
import com.JavaTech.HeadQuarter.model.Branch;
import com.JavaTech.HeadQuarter.service.BranchService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/branch")
public class BranchController {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private BranchService branchService;

    @GetMapping(value = "")
    public String showBranch(Model model){
        model.addAttribute("listBranch", branchService.listAllDTO());
        return "/branch/page-branchs";
    }

    @PostMapping(value = "/add")
    public ResponseEntity<?> addBranch(@RequestParam("name") String name,
                                       @RequestParam("address") String address){
        branchService.saveOrUpdate(Branch.builder()
                .name(name)
                .address(address)
                .createdAt(new Date()).build());

        Map<String, Object> response = new HashMap<>();
        response.put("newBranch", modelMapper.map(branchService.findByName(name), BranchDTO.class));
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/edit/{id}")
    public ResponseEntity<?> editBranch(@PathVariable("id") Long id,
                                        @RequestParam("name") String name,
                                        @RequestParam("address") String address){
        Branch branch = branchService.findByid(id);
        branch.setName(name);
        branch.setAddress(address);
        branch = branchService.saveOrUpdate(branch);
        Map<String, Object> response = new HashMap<>();
        response.put("updatedBranch", modelMapper.map(branch, BranchDTO.class));
        return ResponseEntity.ok(response);
    }
}