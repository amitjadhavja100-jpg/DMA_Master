package com.icici.dma.slabEntity.flows;

public class FooterRule {

    private boolean footerRequired;
    private boolean performanceFooterRequired;

    public FooterRule(
            boolean footerRequired,
            boolean performanceFooterRequired) {

        this.footerRequired = footerRequired;
        this.performanceFooterRequired =
                performanceFooterRequired;
    }

    public boolean isFooterRequired() {
        return footerRequired;
    }

    public boolean isPerformanceFooterRequired() {
        return performanceFooterRequired;
    }
}