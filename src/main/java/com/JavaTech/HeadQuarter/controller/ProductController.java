package com.JavaTech.HeadQuarter.controller;


import com.JavaTech.HeadQuarter.dto.BranchDTO;
import com.JavaTech.HeadQuarter.dto.ProductDTO;
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
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping(value = "/list")
    public String showAll(Model model){
        model.addAttribute("branchList", branchService.listAll());
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

    @PostMapping(value = "/edit/{id}")
    public ResponseEntity<?> editBranch(@PathVariable("id") String id,
                                        @RequestParam("name") String productName,
                                        @RequestParam("importPrice") int importPrice,
                                        @RequestParam("retailPrice") int retailPrice){
        Product product = productService.findById(id);
        product.setName(productName);
        product.setImportPrice(importPrice);
        product.setRetailPrice(retailPrice);
        product = productService.saveOrUpdate(product);
        Map<String, Object> response = new HashMap<>();
        response.put("updatedProduct", modelMapper.map(product, ProductDTO.class));
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/get-by-id")
    public ResponseEntity<?> getById( @RequestParam("id") String id)
                                        {
        Product product = productService.findById(id);
        Map<String, Object> response = new HashMap<>();
        response.put("product", modelMapper.map(product, ProductDTO.class));
        return ResponseEntity.ok(response);
    }
    @PostMapping(value = "/get-by-branch")
    public ResponseEntity<?> getByBranch(@RequestParam("branch") String branch){
        List<ProductDTO> productDTOList = null;
        if(!branch.equals("All")) {
            productDTOList = productService.listAll().stream()
                    .map(product -> {
                        ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);
                        QuantityProduct quantityProduct = quantityProductService.findByBranchAndProduct(branchService.findByName(branch), product);
                        int quantity = (quantityProduct != null) ? quantityProduct.getQuantity() : 0;
                        productDTO.setQuantityOfBranch(quantity);
                        return productDTO;
                    })
                    .collect(Collectors.toList());
        }
        else {
            productDTOList = productService.listAll().stream()
                    .map(product -> {
                        ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);
                        productDTO.setQuantityOfBranch(sumQuantity(product));
                        return productDTO;
                    })
                    .collect(Collectors.toList());
        }

        Map<String, Object> response = new HashMap<>();
        response.put("productList", productDTOList);
        return ResponseEntity.ok(response);
    }

    private int sumQuantity(Product product){
        return quantityProductService.findAllByProduct(product)
                .stream()
                .mapToInt(QuantityProduct::getQuantity)
                .sum();
    }
}
