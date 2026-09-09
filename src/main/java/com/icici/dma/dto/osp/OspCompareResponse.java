package com.icici.dma.dto.osp;

public class OspCompareResponse {

    private OspPayoutCompareDto top;

    private OspPayoutCompareDto bottom;

    public OspPayoutCompareDto getTop() {
        return top;
    }

    public void setTop(OspPayoutCompareDto top) {
        this.top = top;
    }

    public OspPayoutCompareDto getBottom() {
        return bottom;
    }

    public void setBottom(OspPayoutCompareDto bottom) {
        this.bottom = bottom;
    }
}