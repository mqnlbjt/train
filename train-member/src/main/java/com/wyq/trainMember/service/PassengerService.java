package com.wyq.trainMember.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wyq.trainCommon.context.LoginMemberContext;
import com.wyq.trainCommon.exception.BusinessException;
import com.wyq.trainCommon.exception.BusinessExceptionEnum;
import com.wyq.trainCommon.response.PageResp;
import com.wyq.trainCommon.utils.SnowUtil;
import com.wyq.trainMember.domain.entity.Passenger;
import com.wyq.trainMember.domain.entity.PassengerExample;
import com.wyq.trainMember.domain.request.PassengerQueryReq;
import com.wyq.trainMember.domain.request.PassengerSaveReq;
import com.wyq.trainMember.domain.response.PassengerQueryResp;
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
        passenger.setMemberId(LoginMemberContext.getId());
        Passenger passengerDB = getPassenger(passenger);
        if (passengerDB == null) {
            passenger.setId(SnowUtil.getSnowflakeNextId());
            passenger.setCreateTime(now);
            passenger.setUpdateTime(now);
            passengerMapper.insert(passenger);
        } else {
            passenger.setUpdateTime(now);
            passengerMapper.updateByPrimaryKey(passenger);
        }
        //todo 增加身份证正则校验
    }
    public PageResp<PassengerQueryResp> queryList(PassengerQueryReq req) {
        PassengerExample passengerExample = new PassengerExample();
        passengerExample.setOrderByClause("id desc");
        PassengerExample.Criteria criteria = passengerExample.createCriteria();
        if (ObjectUtil.isNotNull(req.getMemberId())) {
            criteria.andMemberIdEqualTo(req.getMemberId());
        }

        LOG.info("查询页码：{}", req.getPage());
        LOG.info("每页条数：{}", req.getSize());
        PageHelper.startPage(req.getPage(), req.getSize());
        List<Passenger> passengerList = passengerMapper.selectByExample(passengerExample);

        PageInfo<Passenger> pageInfo = new PageInfo<>(passengerList);
        LOG.info("总行数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());

        List<PassengerQueryResp> list = BeanUtil.copyToList(passengerList, PassengerQueryResp.class);

        PageResp<PassengerQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);
        return pageResp;
    }

    public void delete(Long id) {
        passengerMapper.deleteByPrimaryKey(id);
    }

    public Passenger getPassenger(Passenger passenger){
        PassengerExample passengerExample = new PassengerExample();
        passengerExample.createCriteria()
                .andMemberIdEqualTo(passenger.getMemberId())
                .andIdCardEqualTo(passenger.getIdCard())
                .andNameEqualTo(passenger.getName());
        List<Passenger> passengers = passengerMapper.selectByExample(passengerExample);
        if (passengers == null){
            throw new BusinessException(BusinessExceptionEnum.MEMBER_MOBILE_EXIST);
        }
        try{
            return passengers.get(0);
        }catch (Exception ignored){

        }
        return null;
    }


}
