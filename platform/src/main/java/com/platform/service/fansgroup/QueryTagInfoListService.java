package com.platform.service.fansgroup;

import com.platform.dto.FansGroupDto;

import java.util.Map;

public interface QueryTagInfoListService {
    Map<String,Object> QueryTagInfoList(FansGroupDto fansGroupDto);
}
