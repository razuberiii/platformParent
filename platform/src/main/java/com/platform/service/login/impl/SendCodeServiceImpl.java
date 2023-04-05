package com.platform.service.login.impl;

import com.platform.service.login.SendCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utils.RSAUtil;
import com.platform.utils.RedisUtil;

import java.util.HashMap;
import java.util.Map;
@Service
public class SendCodeServiceImpl implements SendCodeService {

    @Autowired
    RedisUtil redisUtil;

    @Override
    public Map<String, Object> sendCode(String phoneNumber) throws Exception {
        String phoneNumAfterRSADecrypt = RSAUtil.decrypt(phoneNumber, RSAUtil.getPrivateKey());
        //模拟通过手机号发送验证码123456
        String code = "123456";
        HashMap<String, Object> map = new HashMap<>();
        map.put("code",code);
        redisUtil.setForTimeMIN("phoneCode:"+phoneNumber,code,2);
        return map;
    }
}
