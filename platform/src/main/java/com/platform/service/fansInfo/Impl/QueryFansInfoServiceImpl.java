package com.platform.service.fansInfo.Impl;

import com.platform.dao.FansInfoMapper;
import com.platform.dto.fansInfo.FansInfoDto;
import com.platform.service.fansInfo.QueryFansInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class QueryFansInfoServiceImpl implements QueryFansInfoService {

    @Autowired
    FansInfoMapper fansInfoMapper;

    @Override
    public Map<String, Object> queryFansInfo(FansInfoDto fansInfoDto) {
        HashMap<String, Object> res = new HashMap<>();

        String sex = fansInfoDto.getSex();
        String subscribeScene = fansInfoDto.getSubscribeScene();
        String subscribeTimeStart = fansInfoDto.getSubscribeTimeStart();
        String subscribeTimeEnd = fansInfoDto.getSubscribeTimeEnd();
        String bindStatus = fansInfoDto.getBindStatus();

        if (StringUtils.isEmpty(sex) || StringUtils.isEmpty(subscribeScene) || StringUtils.isEmpty(subscribeTimeStart)
                || StringUtils.isEmpty(subscribeTimeEnd) || StringUtils.isEmpty(bindStatus)) {
            res.put("error", "信息不完整");
            return res;
        }
        if(!(sex.equals("0")||sex.equals("1")||sex.equals("2"))){
            res.put("error", "性别信息错误");
            return res;
        }
        if(!(bindStatus.equals("0")||bindStatus.equals("1"))){
            res.put("error", "绑定信息错误");
            return res;
        }
        if(!(subscribeScene.equals("ADD_SCENE_SEARCH")||subscribeScene.equals("ADD_SCENE_ACCOUNT_MIGRATION")||
                subscribeScene.equals("ADD_SCENE_PROFILE_CARD")||subscribeScene.equals("ADD_SCENE_QR_CODE")||
                subscribeScene.equals("ADD_SCENE_PROFILE_LINK")||subscribeScene.equals("ADD_SCENE_PROFILE_ITEM")||
                subscribeScene.equals("ADD_SCENE_PAID")||subscribeScene.equals("ADD_SCENE_OTHERS"))){
            res.put("error", "关注来源信息错误");
            return res;
        }
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

        Integer fansCount = fansInfoMapper.queryFansInfo(fansInfoDto);
        res.put("fansCount",fansCount);
        return res;
    }
}
