package com.platform.scheduled;

import com.platform.dao.FansInfoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import utils.SendMessageUtil;

import java.util.Date;
import java.util.List;
@Component
@Slf4j
public class ScheduledSendMessage {

    @Autowired
    FansInfoMapper fansInfoMapper;

    @Scheduled(fixedDelay = 1000*60*30)
    @Retryable
    public void sendMessage() {
        long time = new Date().getTime();
        List<String> openidList = fansInfoMapper.sendFirstMessage(time);

        int count;
        if (openidList.size() % 1000 == 0) {
            count = openidList.size() / 1000;
        } else {
            count = openidList.size() / 1000 + 1;
        }

        int flag=0;
        for (int i = 0; i < count; i++) {
            List<String> list;
            if (i == count - 1) {
                list = openidList.subList(i * 1000, i * 1000 + openidList.size() - i * 1000);
            } else {
                list = openidList.subList(i * 1000, i * 1000 + 1000);
            }
            Boolean aBoolean = SendMessageUtil.sendMessageToWeChat(list, "WX_PH_WZ_20200819005050299");

            while(!aBoolean &&flag<3){
                aBoolean = SendMessageUtil.sendMessageToWeChat(openidList, "WX_PH_WZ_20200819005050299");
                flag++;
            }

            if (aBoolean) {
               fansInfoMapper.updateIsSendFirstMessage(openidList);
            }
        }
        log.info("定时推送已完成");
    }
}
