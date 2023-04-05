package com.platform.service.template.impl;

import com.platform.dao.TemplateMapper;
import com.platform.dto.template.TemplateDto;
import com.platform.dto.template.TemplatePo;
import com.platform.service.template.TemplateListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class TemplateListServiceImpl implements TemplateListService {

    @Autowired
    TemplateMapper templateMapper;

    @Override
    public Map<String, Object> templateList(TemplateDto templateDto) {
        HashMap<String, Object> res = new LinkedHashMap<>();

        String templateId = templateDto.getTemplateId();
        String templateName = templateDto.getTemplateName();
        Integer page = templateDto.getPage();
        Integer pageSize = templateDto.getPageSize();

        if(page==null||pageSize==null){
            res.put("error","信息不完整");
            return res;
        }

        templateDto.setStart((page - 1) * pageSize);
        templateDto.setEnd(pageSize);

        if(templateId==null){
            templateDto.setTemplateId("%");
        }

        if(templateName==null){
            templateDto.setTemplateName("%");
        }
        TemplatePo[] templateInfos = templateMapper.templateList(templateDto);
        Integer count = templateMapper.selectCount(templateDto);
        res.put("totalTemplate",count);
        int totalPage;
        if(count%pageSize==0){
            totalPage=count/pageSize;
        }else{
            totalPage=count/pageSize+1;
        }
        res.put("totalPage",totalPage);
        res.put("templateList",templateInfos);

        return res;
    }
}
