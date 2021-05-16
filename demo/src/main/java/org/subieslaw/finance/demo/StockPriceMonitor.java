package org.subieslaw.finance.demo;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class StockPriceMonitor {

    private final StockReader stockReader;
    private final StockEvent stockEvent;
    private final AuditLog auditLog;
    private List<Stock> monitoredStocks = new LinkedList<>();

    BigDecimal readCurrentStockPrice(String stockTicker) {
        Optional<StockInfo> stockInfo = stockReader.get(stockTicker);
        auditLog.record(AuditEvent.STOCK_PRICE_CALLED);
        return stockInfo.isPresent() ? stockInfo.get().price : BigDecimal.valueOf(-1);
    }

    public boolean registerStockForMonitoring(String ticker, BigDecimal minimalPrice) {
        return this.monitoredStocks.add(new Stock(ticker, minimalPrice));
    }

    public void verifyMonitoredStocks() {
        monitoredStocks.stream().forEach(this::verifyStockPrice);
        auditLog.record(AuditEvent.MONITORING_TRIGGERED);
    }

    void verifyStockPrice(Stock stock) {
        BigDecimal currentPrice = readCurrentStockPrice(stock.ticker);
        stockReader.get(stock.ticker);
        if (currentPriceIsValid(currentPrice) && belowLimit(currentPrice, stock.minimalPrice)) {
            stockEvent.sendBelowThresholdNotification(stock.ticker, stock.minimalPrice, currentPrice);
        }
    }

    private boolean currentPriceIsValid(BigDecimal currentPrice) {
        return currentPrice.compareTo(BigDecimal.valueOf(-1)) == 1;
    }

    boolean belowLimit(BigDecimal price, BigDecimal limit) {
        return price.compareTo(limit) == -1;
    }

    @AllArgsConstructor
    private static class Stock {
        private String ticker;
        private BigDecimal minimalPrice;
    }

    static enum AuditEvent {
        MONITORING_TRIGGERED, STOCK_PRICE_CALLED;
    }
}