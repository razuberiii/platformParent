package com.platform.service.login.impl;


import com.platform.service.login.VerifyPicCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.platform.utils.RedisUtil;

@Service
public class VerifyPicCodeServiceImpl implements VerifyPicCodeService {

    @Autowired
    RedisUtil redisUtil;

    @Override
    public Boolean verifyPicCode(String text, String timestamp) {
//        String codeFromDb = picCodeService.findTextByTimestamp(timestamp);
        String codeFromRedis = redisUtil.get("picCode:" + timestamp);
        return text.equals(codeFromRedis);
    }
}
