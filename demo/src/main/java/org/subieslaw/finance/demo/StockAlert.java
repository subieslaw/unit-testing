package org.subieslaw.finance.demo;

import java.math.BigDecimal;

import lombok.Builder;

@Builder
public class StockAlert {
    private String ticker;
    private BigDecimal threshold;
    private BigDecimal price;
    private StockAlertType alertType;    
}

enum StockAlertType {
    LOWER_THEN_EXPECTED, HIGHER_THEN_EXPECTED;
}



