package org.subieslaw.finance.demo;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class StockPriceMonitor {

    private final StockReader stockReader;
    private final StockEvent stockEvent;
    private final AuditLog audtiLog;
    private List<Stock> monitoredStocks = new LinkedList<>();

    public BigDecimal readCurrentStockPrice(String stockTicker) throws IOException {
        StockInfo stockInfo = stockReader.get(stockTicker);
        return stockInfo.getPrice();
    }

    public boolean registerStockForMonitoring(String ticker, BigDecimal minimalPrice)   {
        return this.monitoredStocks.add(new Stock(ticker, minimalPrice));
    }

    public void verifyMonitoredStocks() {
        monitoredStocks.stream().forEach(this::verifyStockPrice);        
    }

    void verifyStockPrice(Stock stock) {
        StockInfo stockInfo = stockReader.get(stock.ticker);
        if (belowLimit(stockInfo, stock.minimalPrice)) {
            stockEvent.sendBelowThresholdNotification(stock.ticker, stock.minimalPrice, stockInfo.price);
        }
    }

    boolean belowLimit(StockInfo stockInfo,BigDecimal limit){
        return stockInfo.price.compareTo(limit) == -1;
    }

    @AllArgsConstructor
    private static class Stock {
        private String ticker;
        private BigDecimal minimalPrice;
    }

}