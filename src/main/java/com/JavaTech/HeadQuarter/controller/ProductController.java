package com.JavaTech.HeadQuarter.controller;


import com.JavaTech.HeadQuarter.model.Branch;
import com.JavaTech.HeadQuarter.model.Brand;
import com.JavaTech.HeadQuarter.model.Product;
import com.JavaTech.HeadQuarter.model.QuantityProduct;
import com.JavaTech.HeadQuarter.service.BranchService;
import com.JavaTech.HeadQuarter.service.BrandService;
import com.JavaTech.HeadQuarter.service.ProductService;
import com.JavaTech.HeadQuarter.service.QuantityProductService;
import com.JavaTech.HeadQuarter.utils.BarcodeUtil;
import com.JavaTech.HeadQuarter.utils.ImageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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

    @Autowired
    private BrandService brandService;

    @GetMapping(value = "/list")
    public String showAll(Model model){
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

    @GetMapping(value = "/add")
    public String showViewAdd(Model model){
        model.addAttribute("listBrand", brandService.listAll());
        return "/product/page-add-products";
    }

    @PostMapping(value = "/add")
    public String addProduct(@RequestParam(name = "name") String name,
                             @RequestParam(name = "importPrice") int importPrice,
                             @RequestParam(name = "retailPrice") int retailPrice,
                             @RequestParam(name = "quantity") int quantity,
                             @RequestParam(name = "barCode") String barCode,
                             @RequestParam(name = "image") MultipartFile image,
                             @RequestParam(name = "brand") String brand,
                             @RequestParam(name = "description") String description) throws IOException {
        //save product
        Brand brand_org = brandService.findByName(brand);

        Product product = Product.builder()
                .name(name)
                .importPrice(importPrice)
                .retailPrice(retailPrice)
                .image(image.getBytes())
                .createdAt(new Date())
                .barCode(barCode)
                .imageBarCode(ImageUtil.convertToBase64(BarcodeUtil.generateCodeBarcode(barCode, name)))
                .brand(brand_org)
                .totalSales(0)
                .description(description)
                .build();
        brand_org.getProducts().add(product);
        brandService.addOrSave(brand_org);
        productService.saveOrUpdate(product);

        //quantity
        for (Branch branch : branchService.listAll()) {
            QuantityProduct quantityProduct = QuantityProduct.builder()
                        .branch(branch)
                        .product(product)
                        .quantity(0)
                        .build();
            quantityProductService.saveOrUpdate(quantityProduct);
        }
        return "redirect:/product/list";
    }
}
