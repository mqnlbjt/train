package com.wyq.trainMember.controller;

import com.wyq.trainCommon.response.CommonResp;
import com.wyq.trainMember.domain.request.PassengerSaveReq;
import com.wyq.trainMember.service.PassengerService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/passenger")
public class PassengerController {

    @Resource
    private PassengerService passengerService;

    @PostMapping("/save")
    public CommonResp<Object> save(@Valid @RequestBody PassengerSaveReq saveReq) {
        passengerService.save(saveReq);
        return new CommonResp<>();
    }


}
