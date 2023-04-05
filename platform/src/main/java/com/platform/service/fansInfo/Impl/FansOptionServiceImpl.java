package com.platform.service.fansInfo.Impl;

import com.platform.service.fansInfo.FansOptionService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class FansOptionServiceImpl implements FansOptionService {
    @Override
    public Map<String, Object> FansOption() {
        LinkedHashMap<String, Object> res = new LinkedHashMap<>();

        Object[] sexObject = new Object[3];
        HashMap<String, Object> sex1 = new HashMap<>();
        sex1.put("value","未知");
        sex1.put("code","0");
        HashMap<String, Object> sex2 = new HashMap<>();
        sexObject[0]=sex1;
        sex2.put("value","男");
        sex2.put("code","1");
        sexObject[1]=sex2;
        HashMap<String, Object> sex3 = new HashMap<>();
        sex3.put("value","女");
        sex3.put("code","2");
        sexObject[2]=sex3;
        res.put("sex",sexObject);

        Object[] bindStatusObject = new Object[2];
        HashMap<String, Object> bindStatus = new HashMap<>();
        bindStatus.put("value","未绑定");
        bindStatus.put("code","0");
        bindStatusObject[0]=bindStatus;
        HashMap<String, Object> bindStatus1 = new HashMap<>();
        bindStatus1.put("value","已绑定");
        bindStatus1.put("code","1");
        bindStatusObject[1]=bindStatus1;
        res.put("bindStatus",bindStatusObject);

        Object[] subscribeSceneObject = new Object[8];
        HashMap<String, Object> subscribeScene = new HashMap<>();
        subscribeScene.put("value","公众号搜索");
        subscribeScene.put("code","ADD_SCENE_SEARCH");
        subscribeSceneObject[0]=subscribeScene;
        HashMap<String, Object> subscribeScene1 = new HashMap<>();
        subscribeScene1.put("value","公众号迁移");
        subscribeScene1.put("code","ADD_SCENE_ACCOUNT_MIGRATION");
        subscribeSceneObject[1]=subscribeScene1;
        HashMap<String, Object> subscribeScene2 = new HashMap<>();
        subscribeScene2.put("value","公众号搜索");
        subscribeScene2.put("code","ADD_SCENE_SEARCH");
        subscribeSceneObject[2]=subscribeScene2;
        HashMap<String, Object> subscribeScene3 = new HashMap<>();
        subscribeScene3.put("value","扫描二维码");
        subscribeScene3.put("code","ADD_SCENE_QR_CODE");
        subscribeSceneObject[3]=subscribeScene3;
        HashMap<String, Object> subscribeScene4 = new HashMap<>();
        subscribeScene4.put("value","图文页内名称点击");
        subscribeScene4.put("code","ADD_SCENE_PROFILE_LINK");
        subscribeSceneObject[4]=subscribeScene4;
        HashMap<String, Object> subscribeScene5 = new HashMap<>();
        subscribeScene5.put("value","图文页右上角菜单");
        subscribeScene5.put("code","ADD_SCENE_PROFILE_ITEM");
        subscribeSceneObject[5]=subscribeScene5;
        HashMap<String, Object> subscribeScene6 = new HashMap<>();
        subscribeScene6.put("value","支付后关注");
        subscribeScene6.put("code","ADD_SCENE_OTHERS");
        subscribeSceneObject[6]=subscribeScene6;
        HashMap<String, Object> subscribeScene7 = new HashMap<>();
        subscribeScene7.put("value","其他");
        subscribeScene7.put("code","ADD_SCENE_OTHERS");
        subscribeSceneObject[7]=subscribeScene7;
        res.put("subscribeScene",subscribeSceneObject);
        return res;
    }
}
