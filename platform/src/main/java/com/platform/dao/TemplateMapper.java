package com.platform.dao;

import com.platform.dto.template.TemplateDto;
import com.platform.dto.template.TemplatePo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface TemplateMapper {

    TemplatePo[] templateList(TemplateDto templateDto);

    Integer add(TemplateDto templateDto);

    Integer selectCount(TemplateDto templateDto);

    @Select("select * from template where template_id=#{id}")
    TemplateDto queryById(String id);

}
