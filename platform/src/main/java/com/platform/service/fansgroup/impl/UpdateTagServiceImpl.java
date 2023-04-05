package com.platform.service.fansgroup.impl;

import com.platform.dao.FansGroupMapper;
import com.platform.dto.FansGroupDto;
import com.platform.service.fansgroup.UpdateTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
@Service
public class UpdateTagServiceImpl implements UpdateTagService {

    @Autowired
    FansGroupMapper fansGroupMapper;
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> UpdateTag(FansGroupDto fansGroupDto) {
        HashMap<String, Object> res = new HashMap<>();
        String description = fansGroupDto.getDescription();
        String tagName = fansGroupDto.getName();
        Integer tagId = fansGroupDto.getTagId();
        Integer type = fansGroupDto.getType();
        if(StringUtils.isEmpty(description)||StringUtils.isEmpty(tagName)||tagId==null||type==null){
            res.put("error","信息不完整");
            return res;
        }
        if(type!=2&&type!=0){
            res.put("error","分组类型错误");
            return res;
        }
        FansGroupDto fansGroupInfo = fansGroupMapper.queryByTagId(tagId);
        if(fansGroupInfo==null){
            res.put("error","id不存在");
            return res;
        }
        Integer primaryType = fansGroupInfo.getTagType();
        if(primaryType!=type){
            if(primaryType==0){
                if(fansGroupMapper.updateTagCount(-1,1)==0){
                    res.put("error","更新失败");
                    return res;
                }
            }else if(primaryType==2){
                if(fansGroupMapper.updateTagCount(1,-1)==0){
                    res.put("error","更新失败");
                    return res;
                };
            }
        }
        FansGroupDto newFansGroupData = new FansGroupDto();
        newFansGroupData.setDescription(description);
        newFansGroupData.setName(tagName);
        newFansGroupData.setTagId(tagId);
        newFansGroupData.setType(type);

        if(fansGroupMapper.updateFansgroupData(newFansGroupData)==0){
            res.put("error","更新失败");
            return res;
        }
        res.put("success","更新成功");
        return res;
    }
}
