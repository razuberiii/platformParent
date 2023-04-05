package com.platform.service.sendMessage;

import com.platform.dto.SendMessageDto;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface SendMessageService {
    Map<String,Object> sendMessage(SendMessageDto sendMessageDto, HttpServletRequest request) throws Exception;
}
