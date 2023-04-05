package com.platform.service.fansInfo;

import com.platform.dto.fansInfo.FansInfoDto;

import java.util.Map;

public interface TagBindingRuleService {
    Map<String,Object> tagBindingRule(FansInfoDto fansInfoDto);
}
