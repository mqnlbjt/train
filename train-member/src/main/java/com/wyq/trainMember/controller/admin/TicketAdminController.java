package com.wyq.trainMember.controller.admin;

import com.wyq.trainCommon.response.CommonResp;
import com.wyq.trainCommon.response.PageResp;
import com.wyq.trainMember.domain.request.TicketQueryReq;
import com.wyq.trainMember.domain.response.TicketQueryResp;
import com.wyq.trainMember.service.TicketService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/ticket")
public class TicketAdminController {

    @Resource
    private TicketService ticketService;

    @GetMapping("/query-list")
    public CommonResp<PageResp<TicketQueryResp>> queryList(@Valid TicketQueryReq req) {
        PageResp<TicketQueryResp> list = ticketService.queryList(req);
        return new CommonResp<>(list);
    }

}
