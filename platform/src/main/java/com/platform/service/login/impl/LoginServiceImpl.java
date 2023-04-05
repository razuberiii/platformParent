package com.platform.service.login.impl;

import com.alibaba.fastjson.JSON;
import com.platform.dto.LoginDto;
import com.platform.service.login.LoginService;
import entity.Token;
import com.platform.dao.LoginMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import utils.GetIpAddressUtil;
import utils.RSAUtil;
import com.platform.utils.RedisUtil;
import utils.SHA256Util;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.PrivateKey;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class LoginServiceImpl implements LoginService {

    @Resource
    LoginMapper loginMapper;
    @Autowired
    VerifyPicCodeServiceImpl verifyPicCodeService;
    @Autowired
    RedisUtil redisUtil;

    @Override
    public Map<String, Object> login(LoginDto loginDto, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HashMap<String, Object> res = new HashMap<>();
        String phone = loginDto.getPhone();
        String password = loginDto.getPassword();
        String timestamp = loginDto.getTimestamp();
        String code = loginDto.getCode();
        String picCode = loginDto.getPicCode();
        if (StringUtils.isEmpty(phone) || StringUtils.isEmpty(password) || StringUtils.isEmpty(timestamp) || StringUtils.isEmpty(code) || StringUtils.isEmpty(picCode)) {
            res.put("error", "参数不能为空");
            return res;
        }
        PrivateKey privateKey = RSAUtil.getPrivateKey();
        String phoneAfterDecrypted = null;
        String passwordAfterDecrypted = null;
        try {
             phoneAfterDecrypted = RSAUtil.decrypt(phone, privateKey);
             passwordAfterDecrypted = RSAUtil.decrypt(password, privateKey);
        } catch (Exception e) {
            res.put("error", "RSA加密异常");
            return res;
        }

        LoginDto userInfo = loginMapper.findUserByPhone(phoneAfterDecrypted);
        if(userInfo==null){
            res.put("error", "用户不存在");
            return res;
        }
        String salt = userInfo.getSalt();
        String passwordAfterSHA256 = SHA256Util.getSHA256Str(passwordAfterDecrypted + salt);

        if (!passwordAfterSHA256.equals(userInfo.getPassword())) {
            res.put("error", "手机号或密码错误");
            return res;
        }
        if (!verifyPicCodeService.verifyPicCode(picCode, timestamp)) {
            res.put("error", "验证码错误或已过期");
            return res;
        }
        redisUtil.delete("picCode:" + timestamp);
        if (!code.equals(redisUtil.get("phoneCode:" + loginDto.getPhone()))) {
            res.put("error", "手机验证码错误或已过期");
            return res;
        }
        redisUtil.delete("phoneCode:" + loginDto.getPhone());

        HashMap<String, Object> user = new HashMap<>();
        user.put("uid", userInfo.getUid());
        Token token = new Token(String.valueOf(userInfo.getUid()),userInfo.getIpAddress(),userInfo.getUserName());
        String encrypt = RSAUtil.encrypt(JSON.toJSONString(token), RSAUtil.getPublicKey());
        user.put("token", encrypt);
        user.put("phone", phoneAfterDecrypted);
        user.put("loginTime", userInfo.getTime());
        user.put("status", userInfo.getStatus());
        user.put("loginIp", userInfo.getIpAddress());
        user.put("userName", userInfo.getUserName());
        res.put("success", user);
        String ipAddress = GetIpAddressUtil.getClientIpAddr(request);
        Cookie cookie = new Cookie("token",encrypt );
        response.addCookie(cookie);
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        loginMapper.updateIpAndTime(ipAddress,time,userInfo.getUid());
        redisUtil.setForTimeMIN("token"+userInfo.getUid(),userInfo.getIpAddress(),15);
        return res;
    }
}
