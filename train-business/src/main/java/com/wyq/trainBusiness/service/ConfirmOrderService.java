package com.wyq.trainBusiness.service;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wyq.trainBusiness.domain.entity.*;
import com.wyq.trainBusiness.domain.enums.ConfirmOrderStatusEnum;
import com.wyq.trainBusiness.domain.enums.SeatColEnum;
import com.wyq.trainBusiness.domain.enums.SeatTypeEnum;
import com.wyq.trainBusiness.domain.request.ConfirmOrderDoReq;
import com.wyq.trainBusiness.domain.request.ConfirmOrderQueryReq;
import com.wyq.trainBusiness.domain.request.ConfirmOrderTicketReq;
import com.wyq.trainBusiness.domain.response.ConfirmOrderQueryResp;
import com.wyq.trainBusiness.domain.response.StationQueryResp;
import com.wyq.trainBusiness.mapper.ConfirmOrderMapper;
import com.wyq.trainCommon.context.LoginMemberContext;
import com.wyq.trainCommon.exception.BusinessException;
import com.wyq.trainCommon.exception.BusinessExceptionEnum;
import com.wyq.trainCommon.response.PageResp;
import com.wyq.trainCommon.utils.SnowUtil;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ConfirmOrderService {

    private static final Logger LOG = LoggerFactory.getLogger(ConfirmOrderService.class);

    @Resource
    private ConfirmOrderMapper confirmOrderMapper;

    @Resource
    private DailyTrainTicketService dailyTrainTicketService;

    @Resource
    private DailyTrainCarriageService dailyTrainCarriageService;

    @Resource
    private DailyTrainSeatService dailyTrainSeatService;


    public void save(ConfirmOrderDoReq req) {
        DateTime now = DateTime.now();
        ConfirmOrder confirmOrder = BeanUtil.copyProperties(req, ConfirmOrder.class);
        if (ObjectUtil.isNull(confirmOrder.getId())) {
            confirmOrder.setId(SnowUtil.getSnowflakeNextId());
            confirmOrder.setCreateTime(now);
            confirmOrder.setUpdateTime(now);
            confirmOrderMapper.insert(confirmOrder);
        } else {
            confirmOrder.setUpdateTime(now);
            confirmOrderMapper.updateByPrimaryKey(confirmOrder);
        }
    }

    public PageResp<ConfirmOrderQueryResp> queryList(ConfirmOrderQueryReq req) {
        ConfirmOrderExample confirmOrderExample = new ConfirmOrderExample();
        confirmOrderExample.setOrderByClause("id desc");
        ConfirmOrderExample.Criteria criteria = confirmOrderExample.createCriteria();

        LOG.info("查询页码：{}", req.getPage());
        LOG.info("每页条数：{}", req.getSize());
        PageHelper.startPage(req.getPage(), req.getSize());
        List<ConfirmOrder> confirmOrderList = confirmOrderMapper.selectByExample(confirmOrderExample);

        PageInfo<ConfirmOrder> pageInfo = new PageInfo<>(confirmOrderList);
        LOG.info("总行数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());

        List<ConfirmOrderQueryResp> list = BeanUtil.copyToList(confirmOrderList, ConfirmOrderQueryResp.class);

        PageResp<ConfirmOrderQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);
        return pageResp;
    }

    public void delete(Long id) {
        confirmOrderMapper.deleteByPrimaryKey(id);
    }

    public List<ConfirmOrderQueryResp> queryAll() {
        ConfirmOrderExample confirmOrderExample = new ConfirmOrderExample();
        confirmOrderExample.setOrderByClause("name_pinyin asc");
        List<ConfirmOrder> confirmOrderList = confirmOrderMapper.selectByExample(confirmOrderExample);
        return BeanUtil.copyToList(confirmOrderList, ConfirmOrderQueryResp.class);
    }

    public List<StationQueryResp> doConfirm(ConfirmOrderDoReq req) {
        //todo 校验票务信息 车次是否存在 余票是否存在 车次有效期 票数大于0 客户是否购买过本次列车(应该需要一个提醒 前端)

        //保存到订单表
        DateTime now = DateTime.now();
        ConfirmOrder confirmOrder = new ConfirmOrder();
        confirmOrder.setId(SnowUtil.getSnowflakeNextId());
        confirmOrder.setCreateTime(now);
        confirmOrder.setUpdateTime(now);
        confirmOrder.setMemberId(LoginMemberContext.getId());
        confirmOrder.setDate(req.getDate());
        confirmOrder.setTrainCode(req.getTrainCode());
        confirmOrder.setStart(req.getStart());
        confirmOrder.setEnd(req.getEnd());
        confirmOrder.setDailyTrainTicketId(req.getDailyTrainTicketId());
        confirmOrder.setStatus(ConfirmOrderStatusEnum.INIT.getCode());
        List<ConfirmOrderTicketReq> tickets = req.getTickets();
        confirmOrder.setTickets(JSON.toJSONString(tickets));

        confirmOrderMapper.insert(confirmOrder);

        //查询余票记录 得到真实库存 daily-train-ticket
        DailyTrainTicket dailyTrainTicket = dailyTrainTicketService.selectByUnique(req.getDate(), req.getTrainCode(),
                req.getStart(), req.getEnd());
        LOG.info("查出余票记录{}", dailyTrainTicket);
        //扣减库存，判断余票是否足够
        reduceTickets(req, dailyTrainTicket);
        //选座 查车厢获取座位信息 多个选座应该是一个车厢
        ConfirmOrderTicketReq ticketReqOne = tickets.get(0);
        if (StrUtil.isNotBlank(ticketReqOne.getSeat())) {
            LOG.info("本次购票有选座");
            List<SeatColEnum> colEnumList = SeatColEnum.getColsByType(ticketReqOne.getSeatTypeCode());
            LOG.info("本次选座包含的列{}", colEnumList);
            List<String> referSeatList = new ArrayList<>();
            for (int i = 0; i <= 2; i++) {
                for (SeatColEnum seatColEnum : colEnumList) {
                    referSeatList.add(seatColEnum.getCode() + i);
                }
            }
            LOG.info("用于参照的两排座位{}", referSeatList);
            List<Integer> offsetList = new ArrayList<>();
            List<Integer> aboluteOffsetList = new ArrayList<>();
            for (ConfirmOrderTicketReq ticketReq : tickets) {
                int index = referSeatList.indexOf(ticketReq.getSeat());
                aboluteOffsetList.add(index);
            }
            LOG.info("所有座位的绝对偏移值{}", aboluteOffsetList);
            for (Integer index : aboluteOffsetList) {
                int offset = index - aboluteOffsetList.get(0);
                offsetList.add(offset);
            }
            LOG.info("所有座位的相对偏移值{}", aboluteOffsetList);

            getSeat(req.getDate(), req.getTrainCode(), ticketReqOne.getSeatTypeCode(),
                    ticketReqOne.getSeat().split(" ")[0], offsetList
                    ,dailyTrainTicket.getStartIndex(),dailyTrainTicket.getEndIndex());
        } else {
            LOG.info("本次购票无选座");
            for (ConfirmOrderTicketReq ticketReq : tickets) {
                getSeat(req.getDate(), req.getTrainCode(), ticketReq.getSeatTypeCode(),
                        null, null,null,null);
            }
        }
        //使用事务控制多张表 修改座位表的sell字段 1111 -> 0000

        //余票表修改余票

        //增加买票记录

        //确认订单成功
        return null;
    }

    private void getSeat(Date date, String trainCode, String seatType, String column, List<Integer> offsetList
            , Integer startIndex, Integer endIndex) {
        List<DailyTrainCarriage> carriageList = dailyTrainCarriageService.selectBySeatType(date, trainCode, seatType);
        LOG.info("一共查询出{}个车厢", carriageList.size());

        for (DailyTrainCarriage dailyTrainCarriage : carriageList) {
            List<DailyTrainSeat> seatList = dailyTrainSeatService
                    .selectByCarriage(date, trainCode, dailyTrainCarriage.getIndex());

            for (DailyTrainSeat dailyTrainSeat : seatList) {
                boolean isChoose = calSell(dailyTrainSeat, startIndex, endIndex);
                if (isChoose) {
                    LOG.info("选中座位");
                    return;
                } else {
                    LOG.info("未选中座位");
                    continue;
                }
            }

        }

    }

    /*
    计算这个区间是否有座位卖 
     */
    private boolean calSell(DailyTrainSeat dailyTrainSeat, Integer startIndex, Integer endIndex) {
        String sell = dailyTrainSeat.getSell();
        String sellPart = sell.substring(startIndex, endIndex);
        if (Integer.parseInt(sellPart) > 0){
            LOG.info("座位{}在本次车站区间{}~{}已售过票，不可选中该座位", dailyTrainSeat.getCarriageSeatIndex(), startIndex, endIndex);
            return false;
        }else {
            LOG.info("座位{}在本次车站区间{}~{}未售过票，可选中该座位", dailyTrainSeat.getCarriageSeatIndex(), startIndex, endIndex);
            LOG.info("座位{}在本次车站区间{}~{}未售过票，可选中该座位", dailyTrainSeat.getCarriageSeatIndex(), startIndex, endIndex);
            //  111,   111
            String curSell = sellPart.replace('0', '1');
            // 0111,  0111
            curSell = StrUtil.fillBefore(curSell, '0', endIndex);
            // 01110, 01110
            curSell = StrUtil.fillAfter(curSell, '0', sell.length());

            // 当前区间售票信息curSell 01110与库里的已售信息sell 00001按位与，即可得到该座位卖出此票后的售票详情
            // 15(01111), 14(01110 = 01110|00000)
            int newSellInt = NumberUtil.binaryToInt(curSell) | NumberUtil.binaryToInt(sell);
            //  1111,  1110
            String newSell = NumberUtil.getBinaryStr(newSellInt);
            // 01111, 01110
            newSell = StrUtil.fillBefore(newSell, '0', sell.length());
            LOG.info("座位{}被选中，原售票信息：{}，车站区间：{}~{}，即：{}，最终售票信息：{}"
                    , dailyTrainSeat.getCarriageSeatIndex(), sell, startIndex, endIndex, curSell, newSell);
            dailyTrainSeat.setSell(newSell);
            return true;
        }

    }

    private static void reduceTickets(ConfirmOrderDoReq req, DailyTrainTicket dailyTrainTicket) {
        for (ConfirmOrderTicketReq ticketReq : req.getTickets()) {
            String seatTypeCode = ticketReq.getSeatTypeCode();
            SeatTypeEnum seatTypeEnum = EnumUtil.getBy(SeatTypeEnum::getCode, seatTypeCode);

            switch (seatTypeEnum) {
                case YDZ -> {
                    int ydz = dailyTrainTicket.getYdz() - 1;
                    if (ydz < 0) {
                        throw new BusinessException(BusinessExceptionEnum.CONFIRM_ORDER_TICKET_COUNT_ERROR);
                    }
                    dailyTrainTicket.setYdz(ydz);
                }
                case EDZ -> {
                    int edz = dailyTrainTicket.getEdz() - 1;
                    if (edz < 0) {
                        throw new BusinessException(BusinessExceptionEnum.CONFIRM_ORDER_TICKET_COUNT_ERROR);
                    }
                    dailyTrainTicket.setEdz(edz);
                }
                case RW -> {
                    int rw = dailyTrainTicket.getRw() - 1;
                    if (rw < 0) {
                        throw new BusinessException(BusinessExceptionEnum.CONFIRM_ORDER_TICKET_COUNT_ERROR);
                    }
                    dailyTrainTicket.setRw(rw);
                }
                case YW -> {
                    int yw = dailyTrainTicket.getYw() - 1;
                    if (yw < 0) {
                        throw new BusinessException(BusinessExceptionEnum.CONFIRM_ORDER_TICKET_COUNT_ERROR);
                    }
                    dailyTrainTicket.setYw(yw);
                }
            }
        }
    }
}
