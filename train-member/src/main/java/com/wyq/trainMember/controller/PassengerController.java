package com.wyq.trainMember.controller;

import com.wyq.trainCommon.context.LoginMemberContext;
import com.wyq.trainCommon.response.CommonResp;
import com.wyq.trainCommon.response.PageResp;
import com.wyq.trainMember.domain.request.PassengerQueryReq;
import com.wyq.trainMember.domain.request.PassengerSaveReq;
import com.wyq.trainMember.domain.response.PassengerQueryResp;
import com.wyq.trainMember.service.PassengerService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/query-list")
    public CommonResp<PageResp<PassengerQueryResp>> queryList(@Valid PassengerQueryReq req) {
        req.setMemberId(LoginMemberContext.getId());
        PageResp<PassengerQueryResp> list = passengerService.queryList(req);
        return new CommonResp<>(list);
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp<Object> delete(@PathVariable Long id) {
        passengerService.delete(id);
        return new CommonResp<>();
    }
    @GetMapping("/query-mine")
    public CommonResp<List<PassengerQueryResp>> queryMine() {
        List<PassengerQueryResp> list = passengerService.queryMine();
        return new CommonResp<>(list);
    }

    @GetMapping("/init")
    public CommonResp<Object> init() {
        passengerService.init();
        return new CommonResp<>();
    }

}
