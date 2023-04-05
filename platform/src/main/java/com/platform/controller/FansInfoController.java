package com.platform.controller;

import com.platform.constants.Result;
import com.platform.constants.ResultCode;
import com.platform.service.fansInfo.FansOptionService;
import com.platform.service.fansInfo.QueryFansInfoService;
import com.platform.service.fansInfo.TagBindingRuleService;
import com.platform.dto.fansInfo.FansInfoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.text.ParseException;
import java.util.Map;

@RequestMapping(value = "/mp/internet/wechat")
@RestController
public class FansInfoController {
    @Autowired
    FansOptionService fansOptionService;
    @Autowired
    TagBindingRuleService tagBindingRuleService;
    @Autowired
    QueryFansInfoService queryFansInfoService;


    @PostMapping("/fans/option")
    public Result fansOption() {
        Map<String, Object> res = fansOptionService.FansOption();
        return new Result(ResultCode.SUCCESS, "success", res);
    }

    @PostMapping("/fans/queryFansInfo")
    public Result queryFansInfo(@RequestBody FansInfoDto fansInfoDto) throws ParseException {
        Map<String, Object> res = queryFansInfoService.queryFansInfo(fansInfoDto);
        if (res.get("error") != null) {
            String errorMsg = String.valueOf(res.get("error"));
            return new Result(ResultCode.FIILED, errorMsg);
        } else {
            return new Result(ResultCode.SUCCESS,"查询成功",res);
        }
    }

    @PostMapping("/tagBindingRule")
    public Result tagBindingRule(@RequestBody FansInfoDto fansInfoDto) {
        Map<String, Object> res = tagBindingRuleService.tagBindingRule(fansInfoDto);
        if (res.get("success") != null) {
            return new Result(ResultCode.SUCCESS, "更新成功");
        } else {
            String errorMsg = String.valueOf(res.get("error"));
            return new Result(ResultCode.FIILED, errorMsg);
        }
    }


}
