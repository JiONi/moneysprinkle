package com.work.moneysprinkle.result;

import lombok.Data;
import java.util.Map;

@Data
public class SprinkleStatusResult {
    private String sprinkleDate;  // 뿌린 시각 (문자열 형태)
    private Long totalAmount;   // 뿌린 금액
    private Long receivedAmount;// 받기 완료된 금액
    private Map<Long, Integer> receivedInfo;    // 받기 완료된 정보

    public SprinkleStatusResult(String sprinkleDate, Long totalAmount, Map<Long, Integer> receivedInfo, Long receivedAmount){
        this.sprinkleDate = sprinkleDate;
        this.totalAmount = totalAmount;
        this.receivedInfo = receivedInfo;
        this.receivedAmount = receivedAmount;
    }

    public SprinkleStatusResult(){

    }
}
