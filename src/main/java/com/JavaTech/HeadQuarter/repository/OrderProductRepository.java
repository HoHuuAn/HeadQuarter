package com.JavaTech.HeadQuarter.repository;

import com.JavaTech.HeadQuarter.model.Branch;
import com.JavaTech.HeadQuarter.model.Customer;
import com.JavaTech.HeadQuarter.model.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {
    List<OrderProduct> findAllByCustomer(Customer customer);

    List<OrderProduct> findByCreatedAtBetween(Date startDate, Date endDate);

    List<OrderProduct> findByCreatedAtBetweenAndBranch(Date startDate, Date endDate, Branch branch);
}
