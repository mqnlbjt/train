package com.wyq.trainMember.service;

import cn.hutool.core.collection.CollUtil;
import com.wyq.trainCommon.exception.BusinessException;
import com.wyq.trainCommon.exception.BusinessExceptionEnum;
import com.wyq.trainMember.domain.entity.Member;
import com.wyq.trainMember.domain.entity.MemberExample;
import com.wyq.trainMember.domain.request.MemberRegisterReq;
import com.wyq.trainMember.mapper.MemberMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("MemberService")
public class MemberService {
    @Resource
    private MemberMapper memberMapper;

    public int count(){
       return (int) memberMapper.countByExample(null);
    }

    public long register(String mobile){
        MemberExample memberExample =  new MemberExample();
        memberExample.createCriteria().andMobileEqualTo(mobile);
        List<Member> list = memberMapper.selectByExample(memberExample);

        if (CollUtil.isNotEmpty(list)){
            throw new BusinessException(BusinessExceptionEnum.MEMBER_MOBILE_EXIST);
        }

        Member member = new Member();
        member.setId(System.currentTimeMillis());
        member.setMobile(member.getMobile());
        int insert = memberMapper.insert(member);
        return member.getId();
    }
}
