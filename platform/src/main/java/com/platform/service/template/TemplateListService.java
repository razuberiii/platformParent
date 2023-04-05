package com.platform.service.template;

import com.platform.dto.template.TemplateDto;

import java.util.Map;

public interface TemplateListService {
    Map<String,Object> templateList(TemplateDto templateDto);
}
