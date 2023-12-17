package com.JavaTech.HeadQuarter.controller;


import com.JavaTech.HeadQuarter.model.QuantityProduct;
import com.JavaTech.HeadQuarter.service.BranchService;
import com.JavaTech.HeadQuarter.service.ProductService;
import com.JavaTech.HeadQuarter.service.QuantityProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/product")
public class ProductController {
    @Autowired
    private ProductService productService;

    @Autowired
    private QuantityProductService quantityProductService;

    @Autowired
    private BranchService branchService;

    @GetMapping(value = "/list")
    public String showBranch(Model model){
        model.addAttribute("listProducts", productService.listDTO(productService.listAll()));
        return "/product/page-list-products";
    }

    @PostMapping(value = "/update-quantity")
    public ResponseEntity<?> updateQuantity(@RequestParam("productId") String productId,
                                            @RequestParam("quantity") int quantity,
                                            @RequestParam("branch") String branch){
        QuantityProduct quantityProduct = quantityProductService.findByBranchAndProduct(branchService.findByName(branch), productService.findById(productId));
        if( quantityProduct == null ){
            quantityProductService.saveOrUpdate(QuantityProduct.builder()
                            .quantity(quantity)
                            .branch(branchService.findByName(branch))
                            .product(productService.findById(productId))
                            .build());
        } else {
            quantityProduct.setQuantity(quantity);
            quantityProductService.saveOrUpdate(quantityProduct);
        }
        Map<String, Object> response = new HashMap<>();
        return ResponseEntity.ok(response);
    }
}
