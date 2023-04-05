package com.platform.service.template;

import com.platform.dto.template.TemplateDto;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface TemplateAddService {
    Map<String,Object> add(TemplateDto templateDto, HttpServletRequest request) throws Exception;
}
