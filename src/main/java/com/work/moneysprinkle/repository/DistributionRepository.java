package com.work.moneysprinkle.repository;

import com.work.moneysprinkle.domain.MoneyDistribution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DistributionRepository extends JpaRepository<MoneyDistribution, Long> {
    @Transactional(readOnly = true)
    MoneyDistribution findFirstBySprinkleTokenAndReceiveUserId(String token, int userId);
    @Transactional(readOnly = true)
    MoneyDistribution findBySprinkleTokenAndReceiveUserId(String token, int userId);
    @Transactional(readOnly = true)
    List<MoneyDistribution> findBySprinkleTokenAndReceiveUserIdIsNot(String sprinkleToken, int userId);

}
