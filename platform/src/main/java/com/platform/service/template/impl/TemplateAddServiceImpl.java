package com.platform.service.template.impl;

import com.alibaba.fastjson.JSON;
import com.platform.dao.TemplateMapper;
import com.platform.service.template.TemplateAddService;
import com.platform.dto.template.TemplateDto;
import entity.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import utils.RSAUtil;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Service

public class TemplateAddServiceImpl implements TemplateAddService {
    @Autowired
    TemplateMapper templateMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> add(TemplateDto templateDto, HttpServletRequest request) throws Exception {


        HashMap<String, Object> res = new HashMap<>();
        String templateType = templateDto.getTemplateType();
        String templateId = templateDto.getTemplateId();
        String templateContent = templateDto.getTemplateContent();
        String templateName = templateDto.getTemplateName();
        String wxTemplateId = templateDto.getWxTemplateId();

        Cookie[] cookies = request.getCookies();
        for(Cookie cookie:cookies){
            String name = cookie.getName();
            if(name.equals("token")){
                String token = cookie.getValue();
                String tokenAfterDecrypt = RSAUtil.decrypt(token, RSAUtil.getPrivateKey());
                Token tokenObject = JSON.parseObject(tokenAfterDecrypt, Token.class);
                String templateCreatedBy = tokenObject.getUserName();
                templateDto.setTemplateCreatedBy(templateCreatedBy);
            }
        }

        if(StringUtils.isEmpty(templateType)||StringUtils.isEmpty(templateId)||StringUtils.isEmpty(templateName)||StringUtils.isEmpty(templateContent)){
            res.put("error","信息不完整");
            return res;
        }
        if(!(templateType.equals("text")||templateType.equals("template"))){
            res.put("error","模板类型错误");
            return res;
        }

        if(templateType.equals("template")){
            if(StringUtils.isEmpty(wxTemplateId)){
                res.put("error","信息不完整");
                return res;
            }
        }

        if(templateType.equals("text")){
            if(!StringUtils.isEmpty(wxTemplateId)){
                res.put("error","模板类型错误");
                return res;
            }
        }

        if(templateMapper.queryById(templateId)!=null){
            res.put("error","id重复");
            return res;
        }

        if(templateMapper.add(templateDto)==0){
            res.put("error","添加失败");
            return res;
        }

        res.put("success","添加成功");
        return res;
    }
}
