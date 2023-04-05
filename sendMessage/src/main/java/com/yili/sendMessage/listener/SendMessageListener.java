package com.yili.sendMessage.listener;


import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import utils.SendMessageUtil;

import java.text.SimpleDateFormat;
import java.util.*;

@RocketMQMessageListener(topic = "sendMessage" , consumerGroup= "sendMessageGroup")
@Component
@Slf4j
public class SendMessageListener implements RocketMQListener<HashMap<String, Object>> {

    @Autowired
    RocketMQTemplate rocketMQTemplate;

    @Override
    public void onMessage(HashMap<String, Object> message) {
        ArrayList<String> openidList = (ArrayList<String>) message.get("openidList");
        int temp = (int) message.get("temp");
        int count = (int) message.get("count");
        String templateId = (String) message.get("templateId");
        int flag=0;
        HashMap<String, Object> hm = new HashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String str=null;
        for (int i = 0; i < temp; i++) {
            List<String> list;
            if (i == temp - 1) {
                list = openidList.subList(i * 1000, i * 1000 + count - i * 1000);
            } else {
                list = openidList.subList(i * 1000, i * 1000 + 1000);
            }
            Boolean aBoolean = SendMessageUtil.sendMessageToWeChat(list, templateId);

            while(!aBoolean &&flag<3){
                aBoolean = SendMessageUtil.sendMessageToWeChat(openidList, templateId);
                flag++;
            }
            if(str!=null&&!str.equals(aBoolean.toString())){
                hm.put("taskStatus","部分成功");
            }
            str=aBoolean.toString();


        }
        if(hm.get("taskStatus")==null){
            if(str!=null&&str.equals("true")){
                hm.put("taskStatus","全部成功");
                String finishTime = sdf.format(new Date());
                hm.put("finishTime",finishTime);
            }

            if(str!=null&&str.equals("false")){
                hm.put("taskStatus","全部失败");
            }
        }


//        long createTime = (long) message.get("createTime");
//        String createTimeFormat = sdf.format(new Date(createTime));
//        hm.put("createTime",createTimeFormat);
        String taskId = (String) message.get("taskId");
        hm.put("taskId", taskId);

//        String creator = (String) message.get("creator");
//        hm.put("creator",creator);

        Message resultMessage = MessageBuilder.withPayload(hm).build();
        rocketMQTemplate.syncSend("sendMessageResult", resultMessage);

    }
}
