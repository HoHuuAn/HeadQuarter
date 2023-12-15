package com.JavaTech.HeadQuarter.service;

import com.JavaTech.HeadQuarter.model.Branch;
import com.JavaTech.HeadQuarter.model.Customer;
import com.JavaTech.HeadQuarter.model.OrderProduct;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderProductService {
    List<OrderProduct> findAllByCustomer(Customer customer);

    OrderProduct saveOrUpdate(OrderProduct orderProduct);

    OrderProduct findById(Long id);

    List<OrderProduct> getOrdersBetweenDates(Date startDate, Date endDate);

    List<OrderProduct> getOrdersBetweenDatesAndBranch(Date startDate, Date endDate, Branch branch);

    Long calculateTotalProfit(List<OrderProduct> orderProducts);
}
