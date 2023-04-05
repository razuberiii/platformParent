package com.platform.service.fansgroup;

import com.platform.dto.FansGroupDto;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface AddTagService {
    Map<String,Object> addTag(FansGroupDto fansGroupDto, HttpServletRequest request) throws Exception;
}
