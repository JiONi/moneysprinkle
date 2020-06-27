package com.work.moneysprinkle.domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@SequenceGenerator(name="DISTRIBUTION_SEQ_GENERATOR", sequenceName = "DISTRIBUTION_SEQ", initialValue = 1, allocationSize = 1)
@Data
public class MoneyDistribution {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TEAM_SEQ_GENERATOR")
    private long distributionId;

    @Column
    private long moneyAmount;

    @Column
    private int receiveUserId;

    @Column
    private String sprinkleToken;

    public MoneyDistribution(long moneyAmount, String sprinkleToken){
        this.moneyAmount = moneyAmount;
        this.sprinkleToken = sprinkleToken;
    }

    public MoneyDistribution(long moneyAmount, int receiveUserId, String sprinkleToken){
        this.moneyAmount = moneyAmount;
        this.receiveUserId = receiveUserId;
        this.sprinkleToken = sprinkleToken;
    }
    public MoneyDistribution(){

    }
}
