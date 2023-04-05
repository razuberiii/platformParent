package com.platform.dto.template;

import lombok.Data;

@Data
public class TemplateDto {
    String templateType;
    String templateId;
    String templateContent;
    String templateName;
    String templateParams;
    String wxTemplateId;
    Integer page;
    Integer pageSize;
    Integer start;
    Integer end;
    String templateCreatedBy;

}
