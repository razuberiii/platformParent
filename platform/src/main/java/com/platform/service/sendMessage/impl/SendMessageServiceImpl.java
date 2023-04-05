package com.platform.service.sendMessage.impl;

import com.alibaba.fastjson.JSON;
import com.platform.dao.FansGroupMapper;
import com.platform.dao.FansInfoMapper;
import com.platform.dto.FansGroupDto;
import com.platform.dto.SendMessageDto;
import com.platform.dao.TaskListMapper;
import com.platform.dto.fansInfo.FansInfoDto;
import com.platform.service.sendMessage.SendMessageService;
import com.platform.utils.RedisUtil;
import entity.Token;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import utils.RSAUtil;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Service
public class SendMessageServiceImpl implements SendMessageService {

    @Autowired
    FansGroupMapper fansGroupMapper;

    @Autowired
    FansInfoMapper fansInfoMapper;

    @Autowired
    TaskListMapper taskListMapper;

    @Autowired
    RocketMQTemplate rocketMQTemplate;

    @Autowired
    RedisUtil redisUtil;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> sendMessage(SendMessageDto sendMessageDto, HttpServletRequest request) throws Exception {

        Date date = new Date();

        HashMap<String, Object> res = new HashMap<>();

        Integer tagId = sendMessageDto.getTagId();
        String templateId = sendMessageDto.getTemplateId();

        if (tagId == null || StringUtils.isEmpty(templateId)) {
            res.put("error", "信息不完整");
            return res;
        }

        String userName = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            String name = cookie.getName();
            if (name.equals("token")) {
                String tokenBeforeRSA = cookie.getValue();
                String tokenJSON = RSAUtil.decrypt(tokenBeforeRSA, RSAUtil.getPrivateKey());
                Token token = JSON.parseObject(tokenJSON, Token.class);
                userName = token.getUserName();
            }
        }
        String s = redisUtil.get("sendMessage" + userName);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
        if (s != null) {

            res.put("error", "5分钟内只可推送一次，剩余时间:" +
                    simpleDateFormat.format(new Date(300000L - (new Date().getTime() - Long.parseLong(s)))));
            return res;
        }
        redisUtil.setForTimeMIN("sendMessage" + userName, String.valueOf(date.getTime()), 5);

        FansGroupDto fansGroupInfo = fansGroupMapper.queryByTagId(tagId);
        if (fansGroupInfo == null) {
            res.put("error", "tagId有误");
            return res;
        }

        String subscribeTimeStart = fansGroupInfo.getSubscribeTimeStart();
        String subscribeTimeEnd = fansGroupInfo.getSubscribeTimeEnd();
        String sex = fansGroupInfo.getSex();
        String bindStatus = fansGroupInfo.getBindStatus();
        String subscribeScene = fansGroupInfo.getSubscribeScene();

        if (StringUtils.isEmpty(subscribeTimeStart) || StringUtils.isEmpty(subscribeTimeEnd) ||
                StringUtils.isEmpty(sex) || StringUtils.isEmpty(bindStatus) || StringUtils.isEmpty(subscribeScene)) {
            res.put("error", "规则分组查询条件未赋值");
            return res;
        }
        FansInfoDto fansInfoDto = new FansInfoDto();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        subscribeTimeStart = subscribeTimeStart + " 00:00:00";
        subscribeTimeEnd = subscribeTimeEnd + " 23:59:59";
        Date start;
        Date end;
        try {
            start = sdf.parse(subscribeTimeStart);
            end = sdf.parse(subscribeTimeEnd);
        } catch (ParseException e) {
            res.put("error", "时间格式不正确");
            return res;
        }

        long startTime = start.getTime();
        long endTime = end.getTime();

        fansInfoDto.setSubscribeTimeStart(String.valueOf(startTime));
        fansInfoDto.setSubscribeTimeEnd(String.valueOf(endTime));
        fansInfoDto.setSex(sex);
        fansInfoDto.setBindStatus(bindStatus);
        fansInfoDto.setSubscribeScene(subscribeScene);

        Integer count = fansInfoMapper.queryFansInfo(fansInfoDto);
        ArrayList<String> openidList = fansInfoMapper.queryFansOpenid(fansInfoDto);
        int temp;
        if (count % 1000 == 0) {
            temp = count / 1000;
        } else {
            temp = count / 1000 + 1;
        }


        SendMessageDto sendMessageDto1 = new SendMessageDto();
        sendMessageDto1.setCreator(userName);
        String taskId = UUID.randomUUID().toString();
        sendMessageDto1.setTaskId(taskId);
        sendMessageDto1.setCreateTime(sdf.format(date));
        sendMessageDto1.setTaskStatus("未完成");
        sendMessageDto1.setTaskType("SEND_MESSAGE_TO_WX");
        if (taskListMapper.createTaskList(sendMessageDto1) == 0) {
            res.put("error", "推送失败，请重试");
            return res;
        }

        HashMap<String, Object> hm = new HashMap<>();
        hm.put("taskId", taskId);
        hm.put("openidList", openidList);
        hm.put("templateId", templateId);
        hm.put("temp", temp);
        hm.put("count", count);
        Message message = MessageBuilder.withPayload(hm).build();
//        List<Message> messageList = new ArrayList<>();
//        for (int i = 0; i < temp; i++) {
//            List<String> list = new ArrayList<>();
//            if (i == temp - 1) {
//                list = openidList.subList(i * 1000, i * 1000 + count - i * 1000);
//            } else {
//                list = openidList.subList(i * 1000, i * 1000 + 1000);
//            }
//            hm.put("taskId", taskId);
//            hm.put("openidList", list);
//            hm.put("templateId", templateId);
////            hm.put("creatTime",time);
////            hm.put("creator",userName);
//            Message message = MessageBuilder.withPayload(hm).build();
//            messageList.add(message);
//
//        }

        rocketMQTemplate.syncSend("sendMessage", message);
        res.put("success", "创建推送任务消息成功");
        return res;
    }
}
