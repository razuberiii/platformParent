package com.platform.service.fansgroup.impl;

import com.alibaba.fastjson.JSON;
import com.platform.dao.FansGroupMapper;
import com.platform.dto.FansGroupDto;
import com.platform.service.fansgroup.AddTagService;
import entity.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import utils.RSAUtil;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class AddTagServiceImpl implements AddTagService {
    @Autowired
    FansGroupMapper fansGroupMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> addTag(FansGroupDto fansGroupDto, HttpServletRequest request) throws Exception {
        HashMap<String, Object> res = new HashMap<>();
        HashMap<String, String> hm = new HashMap<>();
        Cookie[] cookies = request.getCookies();
        for(Cookie cookie : cookies) {
            hm.put(cookie.getName(),cookie.getValue());
        }
        String token = hm.get("token");
        Token tokenObj = JSON.parseObject(RSAUtil.decrypt(token, RSAUtil.getPrivateKey()), Token.class);
        String createBy = tokenObj.getUserName();
        Integer tagId = fansGroupDto.getTagId();
        String description = fansGroupDto.getDescription();
        String tagName = fansGroupDto.getTagName();
        Integer tagType = fansGroupDto.getType();
        if (StringUtils.isEmpty(description) || StringUtils.isEmpty(tagName) || tagType == null||tagId==null||StringUtils.isEmpty(createBy)) {
            res.put("error", "信息缺失");
            return res;
        }
        FansGroupDto fansGroupInfo = fansGroupMapper.queryByTagId(tagId);
        if(fansGroupInfo!=null){
            res.put("error","id已存在");
            return res;
        }
        FansGroupDto fansGroupData = new FansGroupDto();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        fansGroupData.setDateCreated(sdf.format(new Date()));
        fansGroupData.setTagId(tagId);
        fansGroupData.setCreatedBy(createBy);
        fansGroupData.setTagType(tagType);
        fansGroupData.setDescription(description);
        fansGroupData.setFansCount(0);
        fansGroupData.setTagName(tagName);
        if(tagType==0){
            if(fansGroupMapper.updateTagCount(0, 1)==0){
                res.put("error","创建失败");
                return res;
            }
        }else if(tagType==2){
            if(fansGroupMapper.updateTagCount(1, 0)==0){
                res.put("error","创建失败");
                return res;
            }
        }else{
            res.put("error","分组类型错误");
            return res;
        }
        if(fansGroupMapper.addTag(fansGroupData)==0){
            res.put("error","创建失败");
            return res;
        }

        res.put("success","创建成功");
        return res;
    }
}
