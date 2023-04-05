package com.platform.dto.fansInfo;

import lombok.Data;

@Data
public class FansInfoDto {
    Integer tagId;
    Integer fansCount;
    Rule rule;
    String subscribeTimeStart;
    String subscribeTimeEnd;
    String sex;
    String bindStatus;
    String subscribeScene;
}
