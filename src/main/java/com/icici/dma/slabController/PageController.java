package com.icici.dma.slabController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class PageController {

    @GetMapping("/payout")
    public String payoutPage() {
        return "payout";
    }

    @GetMapping("/historyPage")
    public String historyPage() {
        return "history";
    }
    
    @GetMapping("/checkerPage")
    public String checkerPage() {
        return "checker";
    }
    
    @GetMapping("/checkerHistoryPage")
    public String checkerHistoryPage(){
    return "checkerHistoryPage";
    }
    
    @GetMapping("/payoutCompare")
    public String payoutCompare(){
        return "payoutCompare";
    }
    
    
}

