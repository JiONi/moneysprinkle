package com.work.moneysprinkle.repository;

import com.work.moneysprinkle.domain.MoneySprinkle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

public interface SprinkleRepository extends JpaRepository<MoneySprinkle, String> {
    @Transactional(readOnly = true)
    MoneySprinkle findBySprinkleToken(String sprinkleToken);
    @Transactional(readOnly = true)
    MoneySprinkle findBySprinkleTokenAndReceiveAvailableDateAfter(String sprinkleToken, Date now);
    @Transactional(readOnly = true)
    MoneySprinkle findBySprinkleTokenAndReferAvailableDateAfter(String sprinkleToken, Date now);
}
