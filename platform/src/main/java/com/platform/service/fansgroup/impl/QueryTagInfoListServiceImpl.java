package com.platform.service.fansgroup.impl;

import com.platform.dao.FansGroupMapper;
import com.platform.dto.FansGroupDto;
import com.platform.service.fansgroup.QueryTagInfoListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
@Service
public class QueryTagInfoListServiceImpl implements QueryTagInfoListService {
    @Autowired
    FansGroupMapper fansGroupMapper;
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> QueryTagInfoList(FansGroupDto fansGroupDto) {
        HashMap<String, Object> res = new HashMap<>();
        Integer page = fansGroupDto.getPage();
        Integer pageSize = fansGroupDto.getPageSize();
        if(page==null||pageSize == null){
            res.put("error","信息不完整");
            return res;
        }
        if(pageSize>10||pageSize<0){
            res.put("error","每页条数数量异常");
            return res;
        }
        Integer count = fansGroupMapper.queryTableCount();
        Integer ruleTagCount = fansGroupMapper.queryRuleTagCount();
        Integer localTagCount = fansGroupMapper.queryLocalTagCount();
        int totalPage;
        if(count%pageSize==0){
            totalPage=count/pageSize;
        }else{
            totalPage=count/pageSize+1;
        }
        FansGroupDto[] fansInfo = fansGroupMapper.queryTagInfoList((page - 1) * pageSize , pageSize );

        res.put("ruleTag",ruleTagCount);
        res.put("localTag",localTagCount);
        res.put("totalPage",totalPage);
        ArrayList<HashMap<String,Object>> al = new ArrayList<>();
        for(FansGroupDto fanInfo:fansInfo){
            HashMap<String, Object> tagList = new HashMap<>();
            String dateCreated = fanInfo.getDateCreated();
            Integer tagId = fanInfo.getTagId();
            String createBy = fanInfo.getCreatedBy();
            Integer tagType = fanInfo.getTagType();
            String description = fanInfo.getDescription();
            Integer fansCount = fanInfo.getFansCount();
            String tagName = fanInfo.getTagName();
            tagList.put("dateCreated",dateCreated);
            tagList.put("tagId",tagId);
            tagList.put("createBy",createBy);
            tagList.put("tagType",tagType);
            tagList.put("description",description);
            tagList.put("fansCount",fansCount);
            tagList.put("tagName",tagName);
           al.add(tagList);
        }
        res.put("tagList",al);
        return res;
    }
}
