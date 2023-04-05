package com.platform.dto;

import lombok.Data;

@Data
public class TaskListDto {
    String creator;
    String startTime;
    String endTime;
    String taskType;
    Integer page;
    Integer pageSize;
    String taskId;
    String taskStatus;
    String createTime;
    String finishTime;
    Integer start;
    Integer end;

}
