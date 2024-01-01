package com.JavaTech.HeadQuarter.component;

import com.google.gson.Gson;
import com.JavaTech.HeadQuarter.model.QuantityProduct;
import com.google.gson.JsonObject;
import jakarta.persistence.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class QuantityProductListener {

    private final SimpMessagingTemplate messagingTemplate;

    public QuantityProductListener(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @PostUpdate
    public void onPostUpdate(QuantityProduct quantityProduct) {
        System.out.println("QuantityProduct with ID: " + quantityProduct.getId() + " has been updated.");

        JsonObject jsonResponse = new JsonObject();
        jsonResponse.addProperty("productId", quantityProduct.getProduct().getId());
        jsonResponse.addProperty("branch", quantityProduct.getBranch().getName());
        jsonResponse.addProperty("quantity", quantityProduct.getQuantity());

        messagingTemplate.convertAndSend("/topic/quantity-updates", new Gson().toJson(jsonResponse));
    }
}