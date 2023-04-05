package com.platform.controller;

import com.platform.constants.Result;
import com.platform.constants.ResultCode;
import com.platform.dto.TaskListDto;
import com.platform.service.taskList.GetTaskListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequestMapping(value = "/mp/internet/wechat")
@RestController
public class TaskListController {

    @Autowired
    GetTaskListService getTaskListService;

    @PostMapping("/task/getList")
    public Result getTaskList(@RequestBody TaskListDto taskListDto){
        Map<String, Object> res = getTaskListService.getTaskList(taskListDto);
        if (res.get("error") != null) {
            String errorMsg = String.valueOf(res.get("error"));
            return new Result(ResultCode.FIILED, errorMsg);
        } else {
            return new Result(ResultCode.SUCCESS, "查询成功",res);
        }
    }
}
