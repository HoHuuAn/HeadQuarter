package com.JavaTech.HeadQuarter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
public class OrderProductDTO {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("createdAt")
    private Date createdAt = new Date();

    @JsonProperty("totalAmount")
    private Long totalAmount;

    @JsonProperty("cash")
    private Long cash;

    @JsonProperty("orderItems")
    private List<OrderDetailDTO> orderItems = new ArrayList<>();

    @JsonProperty("nameOfCustomer")
    private String nameOfCustomer;
}

