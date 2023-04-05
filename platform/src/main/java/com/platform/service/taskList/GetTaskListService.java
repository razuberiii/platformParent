package com.platform.service.taskList;

import com.platform.dto.TaskListDto;

import java.util.Map;

public interface GetTaskListService {
    Map<String,Object> getTaskList(TaskListDto taskListDto);
}
