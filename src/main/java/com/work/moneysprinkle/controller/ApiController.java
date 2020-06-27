package com.work.moneysprinkle.controller;

import com.work.moneysprinkle.DTO.SprinkleDTO;
import com.work.moneysprinkle.result.SprinkleStatusResult;
import com.work.moneysprinkle.service.SprinkleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ApiController {
    @Autowired
    private SprinkleService sprinkleService;

    @PostMapping(value="/api/money-sprinkle")
    public ResponseEntity<String> saveSprinkle(@RequestHeader(name = "X-USER-ID") int userId,
                                               @RequestHeader(name = "X-ROOM-ID") String roomId,
                                               @RequestBody SprinkleDTO sprinkleDTO){
        String token = sprinkleService.createSprinkle(userId, roomId, sprinkleDTO.getMoneyAmount(), sprinkleDTO.getPerson());
        return ResponseEntity.status(HttpStatus.OK).body(token);
    }

    @PutMapping(value="/api/money-distribution/{token}")
    public ResponseEntity<Long> runReceive(@RequestHeader(name = "X-USER-ID") int userId,
                                           @RequestHeader(name = "X-ROOM-ID") String roomId,
                                           @PathVariable String token){
        Long receiveAmount = sprinkleService.receiveMoney(token, userId, roomId);
        return ResponseEntity.status(HttpStatus.OK).body(receiveAmount);
    }

    @GetMapping(value="/api/money-sprinkle/{token}")
    public ResponseEntity<SprinkleStatusResult> getSprinkle(@RequestHeader(name = "X-USER-ID") int userId,
                                                            @PathVariable String token){
        SprinkleStatusResult result = sprinkleService.getSprinkleInfo(token, userId);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
