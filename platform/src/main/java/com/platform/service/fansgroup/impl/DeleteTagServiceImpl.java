package com.platform.service.fansgroup.impl;

import com.platform.dao.FansGroupMapper;
import com.platform.dto.FansGroupDto;
import com.platform.service.fansgroup.DeleteTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@Transactional(rollbackFor = Exception.class)
public class DeleteTagServiceImpl implements DeleteTagService {
    @Autowired
    FansGroupMapper fansGroupMapper;
    @Override
    public Map<String, Object> deleteTag(FansGroupDto fansGroupDto) {
        HashMap<String, Object> res = new HashMap<>();
        Integer tagId = fansGroupDto.getTagId();
        if(tagId==null){
            res.put("error","信息不完整");
            return res;
        }
        FansGroupDto fansGroupInfo = fansGroupMapper.queryByTagId(tagId);
        if(fansGroupInfo == null){
            res.put("error","分组不存在");
            return res;
        }
        if(fansGroupMapper.deleteTag(tagId)==0){
            res.put("error","删除失败");
            return res;
        }
        Integer tagType = fansGroupInfo.getTagType();
        if(tagType==0){
            if(fansGroupMapper.updateTagCount(0, -1)==0){
                res.put("error","删除失败");
                return res;
            }
        }else if(tagType == 2){
            if(fansGroupMapper.updateTagCount(-1, 0)==0){
                res.put("error","删除失败");
                return res;
            }
        }

        res.put("success","删除成功");
        return res;
    }
}
