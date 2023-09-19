package com.wyq.trainCommon.Handler;

import com.wyq.trainCommon.exception.BusinessException;
import com.wyq.trainCommon.response.CommonResp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ExceptionHandler {
    private static final Logger LOG = LoggerFactory.getLogger(ExceptionHandler.class);

    /**
     * 所有异常统一处理
     * @param e
     * @return
     */
    @org.springframework.web.bind.annotation.ExceptionHandler(value = Exception.class)
    @ResponseBody
    public CommonResp exceptionHandler(Exception e) throws Exception {

        CommonResp commonResp = new CommonResp();
        LOG.error("系统异常：", e);
        commonResp.setSuccess(false);
        commonResp.setMessage("系统出现异常，请联系管理员");
        return commonResp;
    }

    /*
        自定义异常统一处理
     */
    @org.springframework.web.bind.annotation.ExceptionHandler(value = BusinessException.class)
    @ResponseBody
    public CommonResp exceptionHandler(BusinessException e) {
        CommonResp commonResp = new CommonResp();
        LOG.error("业务异常：{}", e.getE().getDesc());
        commonResp.setSuccess(false);
        commonResp.setMessage(e.getE().getDesc());
        return commonResp;
    }


}
