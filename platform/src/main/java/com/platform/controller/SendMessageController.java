package com.platform.controller;

import com.platform.constants.Result;
import com.platform.constants.ResultCode;
import com.platform.dto.SendMessageDto;
import com.platform.service.sendMessage.SendMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RequestMapping(value = "/mp/internet/wechat")
@RestController
public class SendMessageController {
    @Autowired
    SendMessageService sendMessageService;

    @PostMapping("/template/sendTemplateMsg")
    public Result tagBindingRule(@RequestBody SendMessageDto sendMessageDto, HttpServletRequest request) throws Exception {
        Map<String, Object> res = sendMessageService.sendMessage(sendMessageDto,request);
        if (res.get("success") != null) {
            return new Result(ResultCode.SUCCESS, "创建推送任务消息成功");
        } else {
            String errorMsg = String.valueOf(res.get("error"));
            return new Result(ResultCode.FIILED, errorMsg);
        }
    }
}
