package org.subieslaw.finance.demo;

import org.subieslaw.finance.demo.StockPriceMonitor.AuditEvent;

public interface AuditLog {

    void record(AuditEvent monitoringTriggered);

}
