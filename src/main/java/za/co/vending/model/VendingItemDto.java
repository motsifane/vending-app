package za.co.vending.model;

import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VendingItemDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private double price;
    private double amount;
    private int quantity;

}

