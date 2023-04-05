package com.platform.controller;

import com.platform.constants.Result;
import com.platform.constants.ResultCode;
import com.platform.dto.template.TemplateDto;
import com.platform.service.template.TemplateAddService;
import com.platform.service.template.TemplateListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RequestMapping(value = "/mp/internet/wechat")
@RestController
public class TemplateController {

    @Autowired
    TemplateListService templateListService;

    @Autowired
    TemplateAddService templateAddService;

    @PostMapping("/template/list")
    public Result list(@RequestBody TemplateDto templateDto){
        Map<String, Object> res = templateListService.templateList(templateDto);
        if (res.get("error") != null) {
            String errorMsg = String.valueOf(res.get("error"));
            return new Result(ResultCode.FIILED, errorMsg);
        } else {
            return new Result(ResultCode.SUCCESS, "查询成功",res);
        }
    }

    @PostMapping("/template/add")
    public Result add(@RequestBody TemplateDto templateDto, HttpServletRequest request) throws Exception {
        Map<String, Object> res = templateAddService.add(templateDto,request);
        if(!StringUtils.isEmpty(res.get("success"))){
            return new Result(ResultCode.SUCCESS,"创建成功");
        }
        String errorMsg = String.valueOf(res.get("error"));
        return new Result(ResultCode.FIILED, errorMsg);
    }
}
