package com.work.moneysprinkle.service;

import com.work.moneysprinkle.domain.MoneyDistribution;
import com.work.moneysprinkle.domain.MoneySprinkle;
import com.work.moneysprinkle.exception.*;
import com.work.moneysprinkle.repository.DistributionRepository;
import com.work.moneysprinkle.repository.SprinkleRepository;
import com.work.moneysprinkle.result.SprinkleStatusResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SprinkleService {

    @Autowired
    private CommonService commonService;
    @Autowired
    private SprinkleRepository sprinkleRepository;
    @Autowired
    private DistributionRepository distributionRepository;

    public String createSprinkle(int userId, String roomId, Long moneyAmount, int person){
        // token 발급
        String token = commonService.createSprinkleToken();

        // 뿌리기 건에 대한 정보 저장
        Date now = new Date();
        MoneySprinkle sprinkle = new MoneySprinkle(token, userId, roomId, now, moneyAmount);
        sprinkleRepository.save(sprinkle);

        // 뿌릴 금액을 인원수에 맞게 분배
        long[] distributionMoney = commonService.distribute(moneyAmount, person);

        // 인원수에 맞게 분배한 금액 정보를 저장
        for(int i=0; i<distributionMoney.length; i++){
            MoneyDistribution distribution = new MoneyDistribution(distributionMoney[i], token);
            distributionRepository.save(distribution);
        }

        return token;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public synchronized Long receiveMoney(String token, int userId, String roomId){
        // 받은 token 에 해당하는 뿌리기 건이 유효한 건인지 확인한다.
        Date now = new Date();
        MoneySprinkle sprinkle = sprinkleRepository.findBySprinkleTokenAndReceiveAvailableDateAfter(token, now);

        // 뿌린지 10분이 지난 요청은 받을 수 없다.
        if(sprinkle == null){
            throw new NotAvailableSprinkleException("유효하지 않은 뿌리기 건입니다.");
        }

        // 받은 token 이 발급된 대화방과 동일한 대화방이 아니면 받을 수 없다.
        if(!roomId.equals(sprinkle.getRoomId())){
            throw new NotAvailableSprinkleException("현재 대화방에서 발생한 뿌리기가 아닙니다.");
        }

        // 뿌리기 한 사용자는 분배받을 수 없다.
        if(sprinkle.getUserId() == userId){
            throw new SprinkleSameUserException("뿌린 사람은 돈을 분배받을 수 없습니다.");
        }

        // 현재 token 에 해당하는 뿌리기 건에 대해 금액을 분배받은 적이 있는 사용자는 더 분배 받을 수 없다.
        if(distributionRepository.findBySprinkleTokenAndReceiveUserId(token, userId) != null){
            throw new AlreadyDistributionException("이미 분배받은 뿌리기 건입니다.");
        }

        // 받은 token의 뿌리기 분배 금액 중 아직 누구에게도 할당되지 않은 금액을 찾는다.
        MoneyDistribution distribution = distributionRepository.findFirstBySprinkleTokenAndReceiveUserId(token, 0);
        // 현재 token 에 해당하는 뿌리기 건에 대해 더 받을 수 있는 금액이 없는 경우
        if(distribution == null){
            throw new NotExistRemainMoneyException("더 이상 받을 수 있는 금액이 없습니다.");
        }
        // 받을 회원 id를 저장한다.
        distribution.setReceiveUserId(userId);
        distributionRepository.saveAndFlush(distribution);

        // 받을 금액 return
        return distribution.getMoneyAmount();
    }

    // token 값에 해당하는 뿌리기 건에 대한 상태를 조회한다.
    @Transactional(readOnly = true)
    public SprinkleStatusResult getSprinkleInfo(String token, int userId) {
        Date now = new Date();
        MoneySprinkle sprinkle = sprinkleRepository.findBySprinkleTokenAndReferAvailableDateAfter(token, now);
        // 조회 유효기간이 지났거나 조회 시도하는 사용자가 뿌리기 한 사용자가 아닌 경우 조회가 불가능하다.
        if(sprinkle == null || sprinkle.getUserId() != userId){
            throw new NotAvailableViewException("조회할 수 있는 뿌리기가 아닙니다.");
        }
        // 받기 완료 된 분배 건 리스트를 가져온다.
        List<MoneyDistribution> distributionList = distributionRepository.findBySprinkleTokenAndReceiveUserIdIsNot(token, 0);
        Map<Long, Integer> distributionInfoList = new HashMap<>();

        // 받기 완료 된 정보를 세팅한다.
        Long receivedAmount = Long.valueOf(0);
        for(int i=0; i<distributionList.size(); i++){
            distributionInfoList.put(distributionList.get(i).getMoneyAmount(), distributionList.get(i).getReceiveUserId());
            receivedAmount = receivedAmount + distributionList.get(i).getMoneyAmount();
        }

        // 리턴할 정보를 생성한다.
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-mm-dd HH:mm:ss");
        SprinkleStatusResult result = new SprinkleStatusResult(sdf.format(sprinkle.getSprinkleDate()),
                sprinkle.getTotalAmount(), distributionInfoList, receivedAmount);

        return result;
    }
}
