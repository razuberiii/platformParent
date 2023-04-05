package com.platform.service.login;

import com.platform.dto.LoginDto;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public interface LoginService {

    Map<String, Object> login(LoginDto loginDto, HttpServletRequest request, HttpServletResponse response) throws Exception;
}
