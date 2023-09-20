package com.wyq.trainMember.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.wyq.trainCommon.context.LoginMemberContext;
import com.wyq.trainCommon.utils.SnowUtil;
import com.wyq.trainMember.domain.entity.Passenger;
import com.wyq.trainMember.domain.entity.PassengerExample;
import com.wyq.trainMember.domain.request.PassengerSaveReq;
import com.wyq.trainMember.mapper.MemberMapper;
import com.wyq.trainMember.mapper.PassengerMapper;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PassengerService {
    private static final Logger LOG = LoggerFactory.getLogger(PassengerService.class);
    @Resource
    private PassengerMapper passengerMapper;
    @Resource
    private MemberMapper memberMapper;

    public void save(PassengerSaveReq req) {
        DateTime now = DateTime.now();
        Passenger passenger = BeanUtil.copyProperties(req, Passenger.class);

        Passenger passengerDB = getPassenger(passenger.getId());
        if (ObjectUtil.isNotEmpty(passengerDB)) {
            passenger.setUpdateTime(now);
            passengerMapper.updateByPrimaryKey(passenger);
        } else {
            passenger.setMemberId(LoginMemberContext.getId());
            passenger.setId(SnowUtil.getSnowflakeNextId());
            passenger.setCreateTime(now);
            passenger.setUpdateTime(now);
            passengerMapper.insert(passenger);
        }
        //todo 增加身份证正则校验
    }

    private Passenger getPassenger(Long passengerId) {
        PassengerExample passengerExample = new PassengerExample();
        passengerExample.createCriteria().andIdEqualTo(passengerId);
        List<Passenger> passengers = passengerMapper.selectByExample(passengerExample);
        return passengers.get(0);
    }


}
