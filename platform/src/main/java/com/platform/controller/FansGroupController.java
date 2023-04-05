package com.platform.controller;

import com.platform.constants.Result;
import com.platform.dto.FansGroupDto;
import com.platform.service.fansgroup.*;
import com.platform.constants.ResultCode;
import com.yiliedu.platform.service.fansgroup.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RequestMapping(value = "/mp/internet/wechat")
@RestController
public class FansGroupController {

    @Autowired
    QueryTagInfoListService queryTagInfoListService;
    @Autowired
    AddTagService addTagService;
    @Autowired
    DeleteTagService deleteTagService;
    @Autowired
    UpdateTagService updateTagService;
    @Autowired
    RuleTagDetailService ruleTagService;


    @PostMapping("/queryTagInfoList")
    public Result queryTagInfoList(FansGroupDto fansGroupDto){
        Map<String, Object> res = queryTagInfoListService.QueryTagInfoList(fansGroupDto);
        if (res.get("error") == null) {
            return new Result(ResultCode.SUCCESS, "查询成功",res);
        } else {
            String errorMsg = String.valueOf(res.get("error"));
            return new Result(ResultCode.FIILED, errorMsg);
        }
    }

    @PostMapping("addTag")
    public Result addTag(@RequestBody FansGroupDto fansGroupDto, HttpServletRequest request) throws Exception {
        Map<String, Object> res = addTagService.addTag(fansGroupDto, request);
        if (res.get("success") != null) {
            return new Result(ResultCode.SUCCESS, "新建分组成功");
        } else {
            String errorMsg = String.valueOf(res.get("error"));
            return new Result(ResultCode.FIILED, errorMsg);
        }
    }

    @PostMapping("deleteTag")
    public Result deleteTag(@RequestBody FansGroupDto fansGroupDto) {
        Map<String, Object> res = deleteTagService.deleteTag(fansGroupDto);
        if (res.get("success") != null) {
            return new Result(ResultCode.SUCCESS, "删除分组成功");
        } else {
            String errorMsg = String.valueOf(res.get("error"));
            return new Result(ResultCode.FIILED, errorMsg);
        }
    }

    @PostMapping("updateTag")
    public Result upateTag(@RequestBody FansGroupDto fansGroupDto) {
        Map<String, Object> res = updateTagService.UpdateTag(fansGroupDto);
        if (res.get("success") != null) {
            return new Result(ResultCode.SUCCESS, "更新成功");
        } else {
            String errorMsg = String.valueOf(res.get("error"));
            return new Result(ResultCode.FIILED, errorMsg);
        }
    }

    @PostMapping("ruleTagDetail")
    public Result ruleTagDetail(@RequestBody FansGroupDto fansGroupDto){
        Map<String, Object> res = ruleTagService.ruleTagDetail(fansGroupDto);
        if (res.get("error") == null) {
            return new Result(ResultCode.SUCCESS, "查询成功",res);
        } else {
            String errorMsg = String.valueOf(res.get("error"));
            return new Result(ResultCode.FIILED, errorMsg);
        }
    }
}
