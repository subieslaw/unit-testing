package org.subieslaw.finance.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class StockMonitorTest {

    @Mock
    private StockReader stockReader;

    @Mock
    private StockEvent stockEvent;
    
    @Mock
    private AuditLog audtiLog;

    @InjectMocks
    private StockPriceMonitor stockMonitor;

    @Test
    public void should_register_stock_for_monitoring() {
        //givem
        this.stockMonitor = new StockPriceMonitor(null, null, null);
        //when
        boolean registered = this.stockMonitor.registerStockForMonitoring("XXX", null);
        //then
        assertTrue(registered, "Should register stock for monitoring");
    }

    @Test
    public void should_get_current_stock_price() throws IOException {
        //given
        StockReader stockReaderStub = new StockReader() {
            @Override
            public StockInfo get(String stockTicker) {
                return StockInfo.builder()
                        .ticker(stockTicker)
                        .price(BigDecimal.TEN)
                        .build();
            }
        };
        this.stockMonitor = new StockPriceMonitor(stockReaderStub, null, null);
        //when
        BigDecimal currentPrice = stockMonitor.readCurrentStockPrice("TSLA");
        //then
        assertEquals(currentPrice, BigDecimal.TEN);
    }

    @Test
    public void should_trigger_alert_when_price_under_limit(){
        //given
        this.stockMonitor.registerStockForMonitoring("XXX", BigDecimal.TEN);
        when(stockReader.get("XXX"))
            .thenReturn(StockInfo.builder().ticker("XXX").price(BigDecimal.ONE).build());
        
        //when
        this.stockMonitor.verifyMonitoredStocks();
        
        //then
        verify(stockEvent, times(1)).sendBelowThresholdNotification("XXX", BigDecimal.TEN, BigDecimal.ONE);
    }

    @Test

}