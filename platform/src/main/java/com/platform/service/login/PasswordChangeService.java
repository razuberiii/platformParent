package com.platform.service.login;

import com.platform.dto.LoginDto;

import java.util.Map;

public interface PasswordChangeService {
    Map<String, Object> passwordChangeService(LoginDto loginDto) throws Exception;
}
