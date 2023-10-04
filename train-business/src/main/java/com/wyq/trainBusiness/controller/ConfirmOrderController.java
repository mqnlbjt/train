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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/confirm-order")
public class ConfirmOrderController {

    @Resource
    private ConfirmOrderService confirmOrderService;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @SentinelResource(value = "confirmOrderDo", blockHandler = "doConfirmBlock")
    @PostMapping("/do")
    public CommonResp<Object> doConfirm(@Valid @RequestBody ConfirmOrderDoReq req) {

        // 图形验证码校验
        String imageCodeToken = req.getImageCodeToken();
        String imageCode = req.getImageCode();
        String imageCodeRedis = stringRedisTemplate.opsForValue().get(imageCodeToken);
        if (ObjectUtils.isEmpty(imageCodeRedis)) {
            return new CommonResp<>(false, "验证码已过期", null);
        }
        // 验证码校验，大小写忽略，提升体验，比如Oo Vv Ww容易混
        if (!imageCodeRedis.equalsIgnoreCase(imageCode)) {
            return new CommonResp<>(false, "验证码不正确", null);
        } else {
            // 验证通过后，移除验证码
            stringRedisTemplate.delete(imageCodeToken);
        }

        List<StationQueryResp> list = confirmOrderService.doConfirm(req);
        return new CommonResp<>(list);
    }



    public CommonResp<Object> doConfirmBlock(ConfirmOrderDoReq req, BlockException e) {
        CommonResp<Object> commonResp = new CommonResp<>();
        commonResp.setSuccess(false);
        commonResp.setMessage(BusinessExceptionEnum.CONFIRM_ORDER_FLOW_EXCEPTION.getDesc());
        return commonResp;

    }

}
