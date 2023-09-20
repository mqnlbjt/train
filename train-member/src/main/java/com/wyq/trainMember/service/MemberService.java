package com.wyq.trainMember.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import com.wyq.trainCommon.exception.BusinessException;
import com.wyq.trainCommon.exception.BusinessExceptionEnum;
import com.wyq.trainCommon.utils.JwtUtil;
import com.wyq.trainCommon.utils.SnowUtil;
import com.wyq.trainMember.domain.entity.Member;
import com.wyq.trainMember.domain.entity.MemberExample;
import com.wyq.trainMember.domain.request.MemberLoginReq;
import com.wyq.trainMember.domain.request.MemberSendCodeReq;
import com.wyq.trainMember.domain.response.MemberLoginResp;
import com.wyq.trainMember.mapper.MemberMapper;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service("MemberService")
public class MemberService {
    private static final Logger LOG = LoggerFactory.getLogger(MemberService.class);
    @Resource
    private RedisTemplate<String,String> redisTemplate;
    @Resource
    private MemberMapper memberMapper;

    public int count(){
       return (int) memberMapper.countByExample(null);
    }

    public long register(String mobile){
        Member memberDB = selectByMobile(mobile);

        if (ObjectUtil.isNull(memberDB)){
            throw new BusinessException(BusinessExceptionEnum.MEMBER_MOBILE_EXIST);
        }

        Member member = new Member();
        member.setId(SnowUtil.getSnowflakeNextId());
        member.setMobile(mobile);
        int insert = memberMapper.insert(member);
        return member.getId();
    }

    public void sendCode(MemberSendCodeReq sendCodeReq){
        String mobile = sendCodeReq.getMobile();
        Member memberDB = selectByMobile(mobile);

        if (ObjectUtil.isNull(memberDB)){
            Member member = new Member();
            member.setId(SnowUtil.getSnowflakeNextId());
            member.setMobile(mobile);
            int insert = memberMapper.insert(member);
        }
        String code = RandomUtil.randomString(6);
        LOG.info("短信验证码:{}",code);
        redisTemplate.opsForValue().set(mobile,code,5L, TimeUnit.MINUTES);
        //todo  对接真实的第三方短信服务
    }

    public MemberLoginResp login(MemberLoginReq loginReq) {
        String code = loginReq.getCode();
        String mobile = loginReq.getMobile();
        Member memberDB = selectByMobile(mobile);
        String redisCode = redisTemplate.opsForValue().get(mobile);

        if (ObjectUtil.isNull(memberDB)){
            throw new BusinessException(BusinessExceptionEnum.MEMBER_MOBILE_NOT_EXIST);
        }
        if (!code.equals(redisCode)){
            throw new BusinessException(BusinessExceptionEnum.MEMBER_MOBILE_CODE_ERROR);
        }
        redisTemplate.delete(mobile);
        MemberLoginResp memberLoginResp = BeanUtil.copyProperties(memberDB, MemberLoginResp.class);
        memberLoginResp.setToken(JwtUtil.createToken(memberDB.getId(),mobile));
        return memberLoginResp;
    }

    private Member selectByMobile(String mobile) {
        MemberExample memberExample = new MemberExample();
        memberExample.createCriteria().andMobileEqualTo(mobile);
        List<Member> list = memberMapper.selectByExample(memberExample);
        if (CollUtil.isEmpty(list)) {
            return null;
        } else {
            return list.get(0);
        }
    }
}
