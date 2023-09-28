package com.wyq.trainBusiness.service;

import com.wyq.trainBusiness.domain.entity.ConfirmOrder;
import com.wyq.trainBusiness.domain.entity.DailyTrainSeat;
import com.wyq.trainBusiness.domain.entity.DailyTrainTicket;
import com.wyq.trainBusiness.domain.request.ConfirmOrderTicketReq;
import com.wyq.trainBusiness.mapper.ConfirmOrderMapper;
import com.wyq.trainBusiness.mapper.DailyTrainSeatMapper;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class AfterConfirmOrderService {
    private static final Logger LOG = LoggerFactory.getLogger(AfterConfirmOrderService.class);

    @Resource
    private DailyTrainSeatMapper dailyTrainSeatMapper;


    @Transactional
    public void afterDoConfirm(List<DailyTrainSeat> finalSeatList){
        for (DailyTrainSeat dailyTrainSeat: finalSeatList){
            DailyTrainSeat seatForUpdate = new DailyTrainSeat();
            seatForUpdate.setId(dailyTrainSeat.getId());
            seatForUpdate.setSell(dailyTrainSeat.getSell());
            seatForUpdate.setDate(new Date());
            dailyTrainSeatMapper.updateByPrimaryKeySelective(seatForUpdate);
        }
    }
}
