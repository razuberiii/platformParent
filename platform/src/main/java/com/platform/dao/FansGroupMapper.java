package com.platform.dao;

import com.platform.dto.FansGroupDto;
import com.platform.dto.fansInfo.FansInfoDto;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface FansGroupMapper {
    Integer addTag(FansGroupDto fansGroupDto);

    Integer updateTagCount(@Param("localTag")Integer localTag, @Param("ruleTag")Integer ruleTag);

    @Select("Select * from fansgroup_data where tag_id =#{tagId}")
    FansGroupDto queryByTagId(@Param("tagId")Integer tagId);

    @Select("Select tag_type from fansgroup_data where tag_id =#{tagId}")
    Integer queryTagTypeByTagId(@Param("tagId")Integer tagId);

    @Delete("Delete from fansgroup_data where tag_id=#{tagId}")
    Integer deleteTag(Integer tagId);

    Integer updateFansgroupData(FansGroupDto fansGroupDto);

    @Select("Select count(1) from fansgroup_data")
    Integer queryTableCount();

    @Select("Select rule_tag from fansgroup_info")
    Integer queryRuleTagCount();

    @Select("Select local_tag from fansgroup_info")
    Integer queryLocalTagCount();

    @Select("Select * from fansgroup_data limit #{start},#{end} ")
    FansGroupDto[] queryTagInfoList(@Param("start")Integer start,@Param("end")Integer end);

    Integer tagBindingRule(FansInfoDto fansInfoDto);
}
