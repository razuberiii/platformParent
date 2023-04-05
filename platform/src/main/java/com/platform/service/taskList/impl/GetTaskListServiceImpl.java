package com.platform.service.taskList.impl;

import com.platform.dao.TaskListMapper;
import com.platform.dto.TaskListDto;
import com.platform.service.taskList.GetTaskListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class GetTaskListServiceImpl implements GetTaskListService {

    @Autowired
    TaskListMapper taskListMapper;

    @Override
    public Map<String, Object> getTaskList(TaskListDto taskListDto) {

        HashMap<String, Object> res = new HashMap<>();
        String creator = taskListDto.getCreator();
        String startTime = taskListDto.getStartTime();
        String endTime = taskListDto.getEndTime();
        String taskType = taskListDto.getTaskType();
        Integer page = taskListDto.getPage();
        Integer pageSize = taskListDto.getPageSize();

        if(page==null||pageSize ==null){
            res.put("error","信息不完整");
            return res;
        }
        if(page<=0||pageSize<=0||pageSize>10){
            res.put("error","页数有误");
            return res;
        }


        TaskListDto taskListPo = new TaskListDto();
        if(!StringUtils.isEmpty(creator)){
            taskListPo.setCreator(creator);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if(!StringUtils.isEmpty(startTime)){
            try {
                sdf.parse(startTime);
            } catch (ParseException e) {
                res.put("error","时间格式错误");
                return res;
            }
            taskListPo.setStartTime(startTime+" 00:00:00");
        }

        if(!StringUtils.isEmpty(endTime)){
            try {
                sdf.parse(endTime);
            } catch (ParseException e) {
                res.put("error","时间格式错误");
                return res;
            }
            taskListPo.setEndTime(endTime+" 23:59:59");
        }

        if(!StringUtils.isEmpty(taskType)){
            taskListPo.setTaskType(taskType);
        }
        taskListPo.setEnd(pageSize);
        taskListPo.setStart((page-1)*pageSize);
        List<TaskListDto> TaskListDtos = taskListMapper.queryTask(taskListPo);
        ArrayList<HashMap<String, Object>> taskList = new ArrayList<>();

        for(TaskListDto task:TaskListDtos){
            HashMap<String, Object> hm = new LinkedHashMap<>();

            hm.put("taskId",task.getTaskId());
            hm.put("creator",task.getCreator());
            String taskType1 = task.getTaskType();
            if(taskType1.equals("SEND_MESSAGE_TO_WX")){
                hm.put("taskTitle","微信推送");
            }else{
                hm.put("taskTitle","加入自定义分组");
            }
            hm.put("taskStatus",task.getTaskStatus());
            hm.put("createTime",task.getCreateTime());
            hm.put("finishTime",task.getFinishTime());
            taskList.add(hm);
        }
        Integer count = taskListMapper.queryCount(taskListPo);
        res.put("count",count);
        Integer totalPage;
        if(count%pageSize==0){
            totalPage=count/pageSize;
        }else{
            totalPage=count/pageSize+1;
        }
        res.put("totalPage",totalPage);
        res.put("taskList",taskList);

        return res;
    }
}
