package com.JavaTech.HeadQuarter.controller;

import org.springframework.stereotype.Controller;
import com.JavaTech.HeadQuarter.dto.OrderProductDTO;
import com.JavaTech.HeadQuarter.dto.ProductDTO;
import com.JavaTech.HeadQuarter.model.*;
import com.JavaTech.HeadQuarter.service.*;
import com.JavaTech.HeadQuarter.utils.DateUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/")
public class DashBoardController {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private BranchService branchService;

    @Autowired
    private OrderProductService orderProductService;

    @Autowired
    private QuantityProductService quantityProductService;

    @Autowired
    private ProductService productService;


    @GetMapping
    public String showHome(Model model){
        model.addAttribute("branchList", branchService.listAll());
        return "index";
    }

    @PostMapping
    public ResponseEntity<?> showData(@RequestParam("startDate") String startDateString,
                                      @RequestParam("endDate") String endDateString,
                                      @RequestParam("branch") String branch) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy");
        Date startDate = dateFormat.parse(startDateString);
        Date endDate = dateFormat.parse(endDateString);

        List<OrderProductDTO> orderDTOList = null;
        List<OrderProduct> orderList = null;
        List<ProductDTO> productDTOList = null;
        if(!branch.equals("All")){
            orderList = orderProductService.getOrdersBetweenDatesAndBranch(
                    DateUtils.getStartOfDay(startDate),
                    DateUtils.getEndOfDay(endDate),
                    branchService.findByName(branch));

            orderDTOList = orderList.stream()
                    .map(orderProduct -> {
                        OrderProductDTO orderProductDTO = modelMapper.map(orderProduct, OrderProductDTO.class);
                        orderProductDTO.setNameOfCustomer(orderProduct.getCustomer().getName());
                        return orderProductDTO;
                    })
                    .collect(Collectors.toList());

            productDTOList = productService.listAll().stream()
                    .map(product -> {
                        ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);
                        QuantityProduct quantityProduct = findByProduct(product, branchService.findByName(branch));
                        int quantity = (quantityProduct != null) ? quantityProduct.getQuantity() : 0;
                        productDTO.setQuantityOfBranch(quantity);
                        return productDTO;
                    })
                    .collect(Collectors.toList());
        } else{
            orderList = orderProductService.getOrdersBetweenDates(
                    DateUtils.getStartOfDay(startDate),
                    DateUtils.getEndOfDay(endDate));


            orderDTOList = orderList.stream()
                    .map(orderProduct -> {
                        OrderProductDTO orderProductDTO = modelMapper.map(orderProduct, OrderProductDTO.class);
                        orderProductDTO.setNameOfCustomer(orderProduct.getCustomer().getName());
                        return orderProductDTO;
                    })
                    .collect(Collectors.toList());

            productDTOList = productService.listAll().stream()
                    .map(product -> {
                        ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);
                        productDTO.setQuantityOfBranch(sumQuantity(product));
                        return productDTO;
                    })
                    .collect(Collectors.toList());
        }
        Long totalAmount = calculateTotalSum(orderList);
        Long totalProduct = calculateTotalProductQuantity(orderList);
        Long profit = orderProductService.calculateTotalProfit(orderList);
        Map<String, Object> response = new HashMap<>();
        response.put("orderList", orderDTOList);
        response.put("totalAmount", totalAmount);
        response.put("totalProduct", totalProduct);
        response.put("profit", profit);
        response.put("productList", productDTOList);

        return ResponseEntity.ok(response);
    }

    private int sumQuantity(Product product){
        return quantityProductService.findAllByProduct(product)
                .stream()
                .mapToInt(QuantityProduct::getQuantity)
                .sum();
    }

    private Long calculateTotalSum(List<OrderProduct> orderList) {
        return orderList.stream()
                .mapToLong(OrderProduct::getTotalAmount)
                .sum();
    }

    private Long calculateTotalProductQuantity(List<OrderProduct> orderList) {
        return orderList.stream()
                .flatMap(order -> order.getOrderItems().stream())
                .mapToLong(OrderDetail::getQuantity)
                .sum();
    }

    public QuantityProduct findByProduct(Product product, Branch branch){
        return quantityProductService.findByBranchAndProduct(branch, product);
    }

//    @MessageMapping("/dashboard")
//    @SendTo("/topic/dashboard")
//    public DashboardData updateDashboard(DashboardData data) {

//        return data;
//    }
}
