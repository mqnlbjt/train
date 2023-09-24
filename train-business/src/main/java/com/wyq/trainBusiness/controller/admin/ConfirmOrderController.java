package com.wyq.trainBusiness.controller.admin;


import com.wyq.trainBusiness.domain.request.ConfirmOrderDoReq;
import com.wyq.trainBusiness.domain.response.StationQueryResp;
import com.wyq.trainBusiness.service.ConfirmOrderService;
import com.wyq.trainBusiness.service.StationService;
import com.wyq.trainCommon.response.CommonResp;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/confirm-order")
public class ConfirmOrderController {

    @Resource
    private ConfirmOrderService confirmOrderService;

    @PostMapping("/do")
    public CommonResp<Object> doConfirm(@Valid @RequestBody ConfirmOrderDoReq req) {
        List<StationQueryResp> list = confirmOrderService.doConfirm(req);
        return new CommonResp<>(list);
    }

}
