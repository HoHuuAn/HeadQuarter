package com.JavaTech.HeadQuarter.controller;


import com.JavaTech.HeadQuarter.service.BranchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/branch")
public class BranchController {

    @Autowired
    private BranchService branchService;

    @GetMapping(value = "/list")
    public String showBranch(Model model){
        model.addAttribute("listBranch", branchService.listAllDTO());
        return "/branch/page-list-branchs";
    }
}