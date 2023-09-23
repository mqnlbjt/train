package com.wyq.trainBusiness.controller.admin;


import com.wyq.trainBusiness.domain.request.DailyTrainSeatQueryReq;
import com.wyq.trainBusiness.domain.request.DailyTrainSeatSaveReq;
import com.wyq.trainBusiness.domain.response.DailyTrainSeatQueryResp;
import com.wyq.trainBusiness.service.DailyTrainSeatService;
import com.wyq.trainCommon.response.CommonResp;
import com.wyq.trainCommon.response.PageResp;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/daily-train-seat")
public class DailyTrainSeatAdminController {

    @Resource
    private DailyTrainSeatService dailyTrainSeatService;

    @PostMapping("/save")
    public CommonResp<Object> save(@Valid @RequestBody DailyTrainSeatSaveReq req) {
        dailyTrainSeatService.save(req);
        return new CommonResp<>();
    }

    @GetMapping("/query-list")
    public CommonResp<PageResp<DailyTrainSeatQueryResp>> queryList(@Valid DailyTrainSeatQueryReq req) {
        PageResp<DailyTrainSeatQueryResp> list = dailyTrainSeatService.queryList(req);
        return new CommonResp<>(list);
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp<Object> delete(@PathVariable Long id) {
        dailyTrainSeatService.delete(id);
        return new CommonResp<>();
    }

}
