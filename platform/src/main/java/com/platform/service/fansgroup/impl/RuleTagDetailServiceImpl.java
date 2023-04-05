package com.platform.service.fansgroup.impl;

import com.platform.dao.FansGroupMapper;
import com.platform.dto.FansGroupDto;
import com.platform.service.fansgroup.RuleTagDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class RuleTagDetailServiceImpl implements RuleTagDetailService {
    @Autowired
    FansGroupMapper fansGroupMapper;
    @Override
    public Map<String, Object> ruleTagDetail(FansGroupDto fansGroupDto) {
        HashMap<String, Object> res = new HashMap<>();
        Integer tagId = fansGroupDto.getTagId();
        if(tagId == null){
            res.put("error","信息不完整");
            return res;
        }
        FansGroupDto fansGroupInfo = fansGroupMapper.queryByTagId(tagId);

        String description = fansGroupInfo.getDescription();
        String tagName = fansGroupInfo.getTagName();
        Integer tagId1 = fansGroupInfo.getTagId();
        Integer tagType = fansGroupInfo.getTagType();
        Integer fansCount = fansGroupInfo.getFansCount();
        HashMap<String, Object> tagRule = new HashMap<>();

        res.put("description",description);
        res.put("tagName",tagName);
        res.put("tagId",tagId1);
        res.put("tagType",tagType);
        res.put("fansCount",fansCount);
        if(tagType==2){
            String subscribeTimeStart = fansGroupInfo.getSubscribeTimeStart();
            String subscribeTimeEnd = fansGroupInfo.getSubscribeTimeEnd();
            String sex = fansGroupInfo.getSex();
            String bindStatus = fansGroupInfo.getBindStatus();
            String subscribeScene = fansGroupInfo.getSubscribeScene();

            tagRule.put("subscribeTimeStart",subscribeTimeStart);
            tagRule.put("subscribeTimeEnd",subscribeTimeEnd);
            tagRule.put("sex",sex);
            tagRule.put("bindStatus",bindStatus);
            tagRule.put("subscribeScene",subscribeScene);
            res.put("tagRule",tagRule);
        }


        return res;
    }
}
