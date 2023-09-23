package com.wyq.trainBusiness.controller.admin;


import com.wyq.trainBusiness.domain.request.DailyTrainStationQueryReq;
import com.wyq.trainBusiness.domain.request.DailyTrainStationSaveReq;
import com.wyq.trainBusiness.domain.response.DailyTrainStationQueryResp;
import com.wyq.trainBusiness.service.DailyTrainStationService;
import com.wyq.trainCommon.response.CommonResp;
import com.wyq.trainCommon.response.PageResp;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/daily-train-station")
public class DailyTrainStationAdminController {

    @Resource
    private DailyTrainStationService dailyTrainStationService;

    @PostMapping("/save")
    public CommonResp<Object> save(@Valid @RequestBody DailyTrainStationSaveReq req) {
        dailyTrainStationService.save(req);
        return new CommonResp<>();
    }

    @GetMapping("/query-list")
    public CommonResp<PageResp<DailyTrainStationQueryResp>> queryList(@Valid DailyTrainStationQueryReq req) {
        PageResp<DailyTrainStationQueryResp> list = dailyTrainStationService.queryList(req);
        return new CommonResp<>(list);
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp<Object> delete(@PathVariable Long id) {
        dailyTrainStationService.delete(id);
        return new CommonResp<>();
    }

}
