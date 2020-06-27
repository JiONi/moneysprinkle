package com.work.moneysprinkle.testService;

import com.work.moneysprinkle.domain.MoneyDistribution;
import com.work.moneysprinkle.domain.MoneySprinkle;
import com.work.moneysprinkle.exception.AlreadyDistributionException;
import com.work.moneysprinkle.exception.NotAvailableSprinkleException;
import com.work.moneysprinkle.exception.NotAvailableViewException;
import com.work.moneysprinkle.exception.SprinkleSameUserException;
import com.work.moneysprinkle.repository.DistributionRepository;
import com.work.moneysprinkle.repository.SprinkleRepository;
import com.work.moneysprinkle.result.SprinkleStatusResult;
import com.work.moneysprinkle.service.SprinkleService;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Calendar;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest()
public class SprinkleServiceTest {
    @Autowired
    SprinkleService sprinkleService;
    @Autowired
    SprinkleRepository sprinkleRepository;
    @Autowired
    DistributionRepository distributionRepository;
    private long receivedMoney = 0;

    @Before
    public void 테스트_데이터_등록(){
        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.add(Calendar.MINUTE, -11);
        MoneySprinkle sprinkle1 = new MoneySprinkle("ABC", 123, "abcde", cal.getTime(), 20000);
        MoneySprinkle sprinkle2 = new MoneySprinkle("bhc", 123, "abcde", now, 10000);
        cal.setTime(now);
        cal.add(Calendar.DATE, -8);
        MoneySprinkle sprinkle3 = new MoneySprinkle("DEF", 123, "abcde", cal.getTime(), 30000);
        sprinkleRepository.deleteAll();
        sprinkleRepository.save(sprinkle1);
        sprinkleRepository.save(sprinkle2);
        sprinkleRepository.save(sprinkle3);

        MoneyDistribution distribution1 = new MoneyDistribution(3400, 456, "bhc");
        MoneyDistribution distribution2 = new MoneyDistribution(3100, 135, "bhc");
        MoneyDistribution distribution3 = new MoneyDistribution(3500, 0, "bhc");
        distributionRepository.deleteAll();
        distributionRepository.save(distribution1);
        distributionRepository.save(distribution2);
        distributionRepository.save(distribution3);
    }

    @Test
    public void 뿌리기_건_등록하기(){
        int userId = 123;
        String roomId = "abcde";
        long moneyAmount = 15000;
        int person = 4;

        sprinkleService.createSprinkle(userId, roomId, moneyAmount, person);
    }

    @Test(expected = SprinkleSameUserException.class)
    public void 돈_분배_받기_실패_뿌린_사용자와_동일(){
        int userId = 123;
        String roomId = "abcde";
        sprinkleService.receiveMoney("bhc", userId, roomId);
    }

    @Test(expected = AlreadyDistributionException.class)
    public void 돈_분배_받기_실패_이미_받은_사용자(){
        int userId = 456;
        String roomId = "abcde";
        sprinkleService.receiveMoney("bhc", userId, roomId);
    }

    @Test(expected = NotAvailableSprinkleException.class)
    public void 돈_분배_받기_실패_뿌려진_대화방과_다름(){
        int userId = 255;
        String roomId = "fail";
        sprinkleService.receiveMoney("bhc", userId, roomId);
    }

    @Test(expected = NotAvailableSprinkleException.class)
    public void 돈_분배_받기_실패_10분_지난_뿌리기(){
        int userId = 857;
        String roomId = "abcde";
        sprinkleService.receiveMoney("ABC", userId, roomId);
    }

    @Test
    public void 뿌리기_건_상태_조회(){
        int userId = 123;
        SprinkleStatusResult result = sprinkleService.getSprinkleInfo("bhc", userId);

        Assert.assertNotNull(result);
        Assert.assertNotNull(result.getSprinkleDate());
        Assert.assertNotNull(result.getReceivedAmount());

        for(Long money : result.getReceivedInfo().keySet()){
            receivedMoney += money;
        }

        Assert.assertTrue(receivedMoney == result.getReceivedAmount());
    }

    @Test(expected = NotAvailableViewException.class)
    public void 뿌리기_건_상태_조회_실패_다른_사용자(){
        int userId = 566;
        sprinkleService.getSprinkleInfo("bhc", userId);
    }

    @Test(expected = NotAvailableViewException.class)
    public void 뿌리기_건_상태_조회_실패_7일_지난_뿌리기(){
        int userId = 123;
        sprinkleService.getSprinkleInfo("DEF", userId);
    }
}
