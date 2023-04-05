package com.platform.dto;

import lombok.Data;

@Data
public class SendMessageDto {
    Integer tagId;
    String templateId;
    String taskId;
    String creator;
    String createTime;
    String finishTime;
    String taskStatus;
    String taskType;

}
