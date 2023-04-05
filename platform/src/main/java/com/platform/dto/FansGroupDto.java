package com.platform.dto;

import lombok.Data;
import lombok.ToString;

@Data
public class FansGroupDto {
    Integer page;
    Integer pageSize;
    Integer ruleTag;
    Integer localTag;
    Integer totalPage;
    String dateCreated;
    Integer tagId;
    String createdBy;
    Integer tagType;
    String description;
    Integer fansCount;
    String tagName;
    Integer type;
    String name;
    String subscribeTimeStart;
    String subscribeTimeEnd;
    String sex;
    String bindStatus;
    String subscribeScene;
}
