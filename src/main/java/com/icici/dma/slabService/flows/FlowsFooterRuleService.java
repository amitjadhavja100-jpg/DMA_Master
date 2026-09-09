package com.icici.dma.slabService.flows;

import com.icici.dma.slabEntity.flows.FlowsBucketMaster;
import com.icici.dma.slabEntity.flows.FooterRule;

public interface FlowsFooterRuleService {

    FooterRule getFooterRule(FlowsBucketMaster bucket);
}