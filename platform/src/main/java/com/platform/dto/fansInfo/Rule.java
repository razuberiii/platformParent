package com.platform.dto.fansInfo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Rule {
    String subscribeTimeStart;
    String subscribeTimeEnd;
    String sex;
    String bindStatus;
    String subscribeScene;
}
