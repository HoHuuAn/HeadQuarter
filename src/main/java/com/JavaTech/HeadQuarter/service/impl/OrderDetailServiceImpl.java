package com.JavaTech.HeadQuarter.service.impl;

import com.JavaTech.HeadQuarter.model.OrderDetail;
import com.JavaTech.HeadQuarter.repository.OrderDetailRepository;
import com.JavaTech.HeadQuarter.service.OrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl implements OrderDetailService {
    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Override
    public OrderDetail saveOrUpdate(OrderDetail orderDetail) {
        return orderDetailRepository.save(orderDetail);
    }
}
