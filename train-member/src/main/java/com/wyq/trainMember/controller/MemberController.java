package com.wyq.trainMember.controller;

import com.wyq.trainCommon.response.CommonResp;
import com.wyq.trainMember.domain.request.MemberLoginReq;
import com.wyq.trainMember.domain.request.MemberRegisterReq;
import com.wyq.trainMember.domain.request.MemberSendCodeReq;
import com.wyq.trainMember.domain.response.MemberLoginResp;
import com.wyq.trainMember.service.MemberService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/member")
public class MemberController {
    @Resource
    private MemberService memberService;

    @GetMapping("/count")
    public CommonResp<Integer> count(){
        int count = memberService.count();
        return new CommonResp<>(count);
    }

    @PostMapping("/register")
    public CommonResp<Long> register(@Valid MemberRegisterReq memberRegisterReq){
        long register = memberService.register(memberRegisterReq.getMobile());
        return new CommonResp<>("注册成功",register);
    }

    @PostMapping("/send-code")
    public CommonResp<Long> sendCode(@Valid @RequestBody MemberSendCodeReq sendCodeReq) {
        memberService.sendCode(sendCodeReq);
        return new CommonResp<>();
    }

    @PostMapping("/login")
    public CommonResp<MemberLoginResp> login(@Valid @RequestBody MemberLoginReq req) {
        MemberLoginResp resp = memberService.login(req);
        return new CommonResp<>(resp);
    }
}
