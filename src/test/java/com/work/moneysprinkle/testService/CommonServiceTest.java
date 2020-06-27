package com.work.moneysprinkle.testService;

import com.work.moneysprinkle.service.CommonService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest()
public class CommonServiceTest {
    @Autowired
    private CommonService commonService;

    @Test
    public void 토큰_발급_테스트(){
        String result = commonService.createSprinkleToken();
        Assert.assertTrue(result.length() == 3);
    }

    @Test
    public void 돈_분배_테스트(){
        long testAmount = 10000000;
        int testPerson = 3000;
        long[] result = commonService.distribute(testAmount, testPerson);
        long totalAmount = 0;
        for(int i=0; i<result.length; i++){
            totalAmount += result[i];
        }

        Assert.assertTrue(totalAmount == testAmount);
        Assert.assertTrue(result.length == testPerson);
    }
}
