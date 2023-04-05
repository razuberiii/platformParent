package com.platform.service.login;

import java.util.Map;

public interface SendCodeService {
    Map<String,Object> sendCode(String phoneNumber) throws Exception;
}
