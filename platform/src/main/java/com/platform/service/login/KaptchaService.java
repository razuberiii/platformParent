package com.platform.service.login;


import java.util.Map;

public interface KaptchaService {

    Map<String,Object> generateVerifyCode() throws Exception;
}
