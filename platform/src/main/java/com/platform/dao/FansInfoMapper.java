package com.platform.dao;

import com.platform.dto.fansInfo.FansInfoDto;
import com.platform.dto.fansInfo.FansInfoPo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface FansInfoMapper {

    void insertFansInfo(FansInfoPo fansInfoPo);

    Integer queryFansInfo(FansInfoDto fansInfoDto);

    ArrayList<String> queryFansOpenid(FansInfoDto fansInfoDto);

    List<String> sendFirstMessage(Long nowTime);

    void updateIsSendFirstMessage(@Param("List") List<String> list);
}
