package com.wyq.trainBatch.job;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.wyq.trainBatch.feign.businessFeign;
import com.wyq.trainCommon.response.CommonResp;
import jakarta.annotation.Resource;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class dailyTrainJob implements Job {
    private static final Logger LOG = LoggerFactory.getLogger(dailyTrainJob.class);
    @Resource
    private com.wyq.trainBatch.feign.businessFeign businessFeign;
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        LOG.info("开始每日定时任务");
        Date date = new Date();
        DateTime dateTime = DateUtil.offsetDay(date, 15);
        Date jdkDate = dateTime.toJdkDate();
        CommonResp<Object> commonResp = businessFeign.genDaily(jdkDate);
        LOG.info("每日定时任务结束,{}",commonResp);
    }
}
