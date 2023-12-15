package com.JavaTech.HeadQuarter.service.impl;

import com.JavaTech.HeadQuarter.model.*;
import com.JavaTech.HeadQuarter.repository.OrderProductRepository;
import com.JavaTech.HeadQuarter.service.OrderProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderProductServiceImpl implements OrderProductService {
    @Autowired
    private OrderProductRepository orderProductRepository;

    @Override
    public List<OrderProduct> findAllByCustomer(Customer customer) {
        return orderProductRepository.findAllByCustomer(customer);
    }

    @Override
    public OrderProduct saveOrUpdate(OrderProduct orderProduct) {
        return orderProductRepository.save(orderProduct);
    }

    @Override
    public OrderProduct findById(Long id) {
        return orderProductRepository.findById(id).orElse(null);
    }

    @Override
    public List<OrderProduct> getOrdersBetweenDates(Date startDate, Date endDate) {
        return orderProductRepository.findByCreatedAtBetween(startDate, endDate);
    }

    @Override
    public List<OrderProduct> getOrdersBetweenDatesAndBranch(Date startDate, Date endDate, Branch branch) {
        return orderProductRepository.findByCreatedAtBetweenAndBranch(startDate, endDate, branch);
    }

    @Override
    public Long calculateTotalProfit(List<OrderProduct> orderProducts) {
        long totalProfit = 0L;
        for (OrderProduct orderProduct : orderProducts) {
            List<OrderDetail> orderDetails = orderProduct.getOrderItems();
            for (OrderDetail orderDetail : orderDetails) {
                Product product = orderDetail.getProduct();
                int profit = (product.getRetailPrice() - product.getImportPrice()) * orderDetail.getQuantity();
                totalProfit += profit;
            }
        }
        return totalProfit;
    }

//    @Override
//    public Map<String, Long> getSumTotalAmountByMonth(Branch branch) {
//        Calendar calendar = Calendar.getInstance();
//        calendar.add(Calendar.MONTH, -11);
//        Date startDate = calendar.getTime();
//        Date endDate = new Date();
//
//        List<OrderProduct> orderProducts = orderProductRepository.findByCreatedAtBetweenAndBranch(startDate, endDate, branch);
//
//        return orderProducts.stream()
//                .collect(Collectors.groupingBy(orderProduct -> {
//                    Calendar orderCalendar = Calendar.getInstance();
//                    orderCalendar.setTime(orderProduct.getCreatedAt());
//                    int year = orderCalendar.get(Calendar.YEAR);
//                    int month = orderCalendar.get(Calendar.MONTH);
//                    return String.format("%04d-%02d", year, month);
//                }, Collectors.summingLong(OrderProduct::getTotalAmount)));
//    }
}