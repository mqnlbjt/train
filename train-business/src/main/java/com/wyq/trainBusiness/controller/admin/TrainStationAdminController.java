package com.wyq.trainBusiness.controller.admin;


import com.wyq.trainBusiness.domain.request.TrainStationQueryReq;
import com.wyq.trainBusiness.domain.request.TrainStationSaveReq;
import com.wyq.trainBusiness.domain.response.TrainStationQueryResp;
import com.wyq.trainBusiness.service.TrainStationService;
import com.wyq.trainCommon.response.CommonResp;
import com.wyq.trainCommon.response.PageResp;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/train-station")
public class TrainStationAdminController {

    @Resource
    private TrainStationService trainStationService;

    @PostMapping("/save")
    public CommonResp<Object> save(@Valid @RequestBody TrainStationSaveReq req) {
        trainStationService.save(req);
        return new CommonResp<>();
    }

    @GetMapping("/query-list")
    public CommonResp<PageResp<TrainStationQueryResp>> queryList(@Valid TrainStationQueryReq req) {
        PageResp<TrainStationQueryResp> list = trainStationService.queryList(req);
        return new CommonResp<>(list);
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp<Object> delete(@PathVariable Long id) {
        trainStationService.delete(id);
        return new CommonResp<>();
    }

}
