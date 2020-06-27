package com.work.moneysprinkle.service;

import com.work.moneysprinkle.repository.SprinkleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class CommonService {
    @Autowired
    private SprinkleRepository sprinkleRepository;

    // 3자리 문자열의 뿌리기 token 발급
    public String createSprinkleToken(){
        Random random = new Random();
        String token = "";
        StringBuffer sb = new StringBuffer();

        while(true){
            for(int i=0; i<3; i++){
                int index = random.nextInt(2);
                switch (index){
                    case 0 : sb.append((char)(random.nextInt(26) + 97)); break;
                    case 1 : sb.append((char)(random.nextInt(26) + 65)); break;
                }
            }
            token = sb.toString();
            if(sprinkleRepository.findBySprinkleToken(token) == null){
                break;
            }
            sb = new StringBuffer();
        }

        return token;
    }

    // 받은 금액을 받은 인원 수 만큼 분배하기
    public long[] distribute(long moneyAmount, int person){
        long distributeComp = 0;
        long[] moneyArr = new long[person];
        Random random = new Random();
        long baseMoney = moneyAmount/(person*2);

        for(int i=0; i<person; i++){
            moneyArr[i] = baseMoney;
            distributeComp += baseMoney;
        }
        for(int i=(person*2)-person; i>0; i--){
            int choicePerson = random.nextInt(person);
            if(i == 1){
                baseMoney = moneyAmount - distributeComp;
            }
            moneyArr[choicePerson] = moneyArr[choicePerson]+baseMoney;
            distributeComp += baseMoney;
        }

        return moneyArr;
    }
}
