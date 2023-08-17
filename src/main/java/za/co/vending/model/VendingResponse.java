package za.co.vending.model;

import lombok.*;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VendingResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    private boolean successful;
    private String message;
    private List<VendingItemDto> items;
    private Map<String, Integer> cart;
    private double totalPrice;
    private double change;
}
