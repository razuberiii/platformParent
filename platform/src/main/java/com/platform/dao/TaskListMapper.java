package com.platform.dao;

import com.platform.dto.SendMessageDto;
import com.platform.dto.TaskListDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TaskListMapper {


    Integer createTaskList(SendMessageDto sendMessageDto);

    void updateTaskList(SendMessageDto sendMessageDto);

    List<TaskListDto> queryTask(TaskListDto taskListDto);


    Integer queryCount(TaskListDto taskListDto);
}
