package com.platform.service.fansInfo.Impl;

import com.platform.dao.FansGroupMapper;
import com.platform.dto.fansInfo.FansInfoDto;
import com.platform.dto.fansInfo.Rule;
import com.platform.service.fansInfo.TagBindingRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
public class TagBindingRuleServiceImpl implements TagBindingRuleService {

    @Autowired
    FansGroupMapper fansGroupMapper;
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> tagBindingRule(FansInfoDto fansInfoDto) {
        HashMap<String, Object> res = new HashMap<>();

        Integer fansCount = fansInfoDto.getFansCount();
        Integer tagId = fansInfoDto.getTagId();
        Rule rule = fansInfoDto.getRule();
        if(fansCount==null||tagId==null||rule==null){
            res.put("error","信息不完整");
            return res;
        }
        Integer tagType = fansGroupMapper.queryTagTypeByTagId(tagId);
        if(tagType==null||tagType!=2){
            res.put("error","id错误");
            return res;
        }

        fansInfoDto.setSubscribeTimeStart(rule.getSubscribeTimeStart());
        fansInfoDto.setSubscribeTimeEnd(rule.getSubscribeTimeEnd());
        fansInfoDto.setSex(rule.getSex());
        fansInfoDto.setBindStatus(rule.getBindStatus());
        fansInfoDto.setSubscribeScene(rule.getSubscribeScene());

        if(fansGroupMapper.tagBindingRule(fansInfoDto)==0){
            res.put("error","更新失败");
            return res;
        }
            res.put("success","更新成功");
        return res;
    }
}
