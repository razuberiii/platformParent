package com.platform.service.fansInfo;

import com.platform.dto.fansInfo.FansInfoDto;

import java.text.ParseException;
import java.util.Map;

public interface QueryFansInfoService {
    Map<String,Object> queryFansInfo(FansInfoDto fansInfoDto) throws ParseException;
}
