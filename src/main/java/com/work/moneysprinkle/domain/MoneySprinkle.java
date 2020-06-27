package com.work.moneysprinkle.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Calendar;
import java.util.Date;

@Entity
@Data
public class MoneySprinkle {

    @Id
    private String sprinkleToken;

    @Column
    private int userId;

    @Column
    private String roomId;

    @Column
    private Date sprinkleDate;

    @Column
    private Date receiveAvailableDate;

    @Column
    private Date referAvailableDate;

    @Column
    private Long totalAmount;

    public MoneySprinkle(String sprinkleToken, int userId, String roomId, Date sprinkleDate, long totalAmount){
        this.sprinkleToken = sprinkleToken;
        this.userId = userId;
        this.roomId = roomId;
        this.sprinkleDate = sprinkleDate;
        this.totalAmount = totalAmount;

        Calendar cal = Calendar.getInstance();
        cal.setTime(sprinkleDate);
        cal.add(Calendar.MINUTE, 10);
        this.receiveAvailableDate = cal.getTime();
        cal.setTime(sprinkleDate);
        cal.add(Calendar.DATE, 7);
        this.referAvailableDate = cal.getTime();
    }

    public MoneySprinkle(){

    }
}
