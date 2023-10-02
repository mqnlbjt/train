package com.wyq.trainBusiness.controller;


import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.wyq.trainBusiness.domain.request.ConfirmOrderDoReq;
import com.wyq.trainBusiness.domain.response.StationQueryResp;
import com.wyq.trainBusiness.service.ConfirmOrderService;
import com.wyq.trainBusiness.service.StationService;
import com.wyq.trainCommon.exception.BusinessException;
import com.wyq.trainCommon.exception.BusinessExceptionEnum;
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

    @SentinelResource(value = "do",blockHandler = "doBlock")
    @PostMapping("/do")
    public CommonResp<Object> doConfirm(@Valid @RequestBody ConfirmOrderDoReq req) {
        List<StationQueryResp> list = confirmOrderService.doConfirm(req);
        return new CommonResp<>(list);
    }
    public CommonResp<Object> doBlock(ConfirmOrderDoReq req, BlockException e) {
        throw new BusinessException(BusinessExceptionEnum.CONFIRM_ORDER_FLOW_EXCEPTION);
    }

}
