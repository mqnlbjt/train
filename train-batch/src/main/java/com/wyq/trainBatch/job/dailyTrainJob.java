package com.wyq.trainBatch.job;

import com.wyq.trainBatch.batchApplication;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class dailyTrainJob implements Job {
    private static final Logger LOG = LoggerFactory.getLogger(dailyTrainJob.class);
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        LOG.info("开始每日定时任务");


        LOG.info("每日定时任务结束");
    }
}
