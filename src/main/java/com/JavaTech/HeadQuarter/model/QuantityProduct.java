package com.JavaTech.HeadQuarter.model;

//import com.JavaTech.HeadQuarter.component.QuantityProductListener;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "quantity_product")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
//@EntityListeners(QuantityProductListener.class)
public class QuantityProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "branch_id", referencedColumnName = "branch_id")
    private Branch branch;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "product_id")
    private Product product;

    @Column(name = "quantity")
    private int quantity;
}
