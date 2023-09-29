package com.wyq.trainMember.controller;

import com.wyq.trainCommon.context.LoginMemberContext;
import com.wyq.trainCommon.response.CommonResp;
import com.wyq.trainCommon.response.PageResp;
import com.wyq.trainMember.domain.request.TicketQueryReq;
import com.wyq.trainMember.domain.response.TicketQueryResp;
import com.wyq.trainMember.service.TicketService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

;

@RestController
@RequestMapping("/ticket")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @GetMapping("/query-list")
    public CommonResp<PageResp<TicketQueryResp>> query(@Valid TicketQueryReq req) {
        CommonResp<PageResp<TicketQueryResp>> commonResp = new CommonResp<>();
        req.setMemberId(LoginMemberContext.getId());
        PageResp<TicketQueryResp> pageResp = ticketService.queryList(req);
        commonResp.setContent(pageResp);
        return commonResp;
    }

}
