package org.subieslaw.finance.demo;

import java.util.Optional;

public interface StockReader {

    Optional<StockInfo> get(String stockTicker);

}
