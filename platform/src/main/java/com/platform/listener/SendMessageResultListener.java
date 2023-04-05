package com.platform.listener;

import com.platform.dto.SendMessageDto;
import com.platform.dao.TaskListMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@RocketMQMessageListener(topic = "sendMessageResult" , consumerGroup= "sendMessageResultGroup")
@Component
@Slf4j
public class SendMessageResultListener implements RocketMQListener<HashMap<String, Object>> {

    @Autowired
    TaskListMapper taskListMapper;

    @Override
    public void onMessage(HashMap<String, Object> message) {

        String taskId = (String) message.get("taskId");
//        String creator = (String) message.get("creator");
        String taskStatus = (String) message.get("taskStatus");
//        String createTime = (String) message.get("createTime");
        String finishTime = (String) message.get("finishTime");

        SendMessageDto sendMessageDto = new SendMessageDto();
        sendMessageDto.setTaskId(taskId);
        if(taskStatus.equals("全部失败")){
            sendMessageDto.setTaskStatus("全部失败");
            taskListMapper.updateTaskList(sendMessageDto);
            return;
        }
        if(taskStatus.equals("全部成功")){
            sendMessageDto.setTaskStatus("全部成功");
            sendMessageDto.setFinishTime(finishTime);
            taskListMapper.updateTaskList(sendMessageDto);
            return;
        }

        sendMessageDto.setTaskStatus("部分成功");
        taskListMapper.updateTaskList(sendMessageDto);

    }
}
