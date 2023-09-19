package com.wyq.trainMember.controller;

import com.wyq.trainCommon.response.CommonResp;
import com.wyq.trainMember.domain.request.MemberRegisterReq;
import com.wyq.trainMember.service.MemberService;
import jakarta.annotation.Resource;
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
    public CommonResp<Long> register( MemberRegisterReq memberRegisterReq){
        long register = memberService.register(memberRegisterReq.getMobile());
        return new CommonResp<>("注册成功",register);
    }
}
