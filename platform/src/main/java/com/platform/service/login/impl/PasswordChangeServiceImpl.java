package com.platform.service.login.impl;

import com.platform.dto.LoginDto;
import com.platform.dao.LoginMapper;
import com.platform.service.login.PasswordChangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import utils.RSAUtil;
import utils.SHA256Util;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Service
public class PasswordChangeServiceImpl implements PasswordChangeService {
    @Autowired
    LoginMapper loginMapper;


    @Override
    @Transactional
    public Map<String, Object> passwordChangeService(LoginDto loginDto) throws Exception {
        HashMap<String, Object> res = new HashMap<>();
        String password = loginDto.getPassword();
        String newPassword = loginDto.getNewPassword();
        String phone = loginDto.getPhone();
        String phoneAfterRSADecrypt = RSAUtil.decrypt(phone, RSAUtil.getPrivateKey());
        if (StringUtils.isEmpty(password) || StringUtils.isEmpty(newPassword) || StringUtils.isEmpty(phone)) {
            res.put("error", "参数缺失");
            return res;
        }

        String newPasswordAfterRSADecrypt = RSAUtil.decrypt(loginDto.getNewPassword(), RSAUtil.getPrivateKey());
        LoginDto userInfo = loginMapper.findUserByPhone(phoneAfterRSADecrypt);
        if (userInfo == null) {
            res.put("error", "手机号不正确");
            return res;
        }
        String passwordFromDB = userInfo.getPassword();
        try {
            if (!passwordFromDB.equals(SHA256Util.getSHA256Str(RSAUtil.decrypt(password, RSAUtil.getPrivateKey()) + userInfo.getSalt()))) {
                res.put("error", "密码不正确");
                return res;
            }
        } catch (Exception e) {
            res.put("error", "RSA错误");
            return res;
        }

        Integer count = userInfo.getCount();
        if (count == null || count < 1 || count > 5) {
            userInfo.setCount(1);
            count = 1;
        }

        String sha256Password = SHA256Util.getSHA256Str(newPasswordAfterRSADecrypt + userInfo.getSalt());
        if (userInfo.getP5().equals(sha256Password) || userInfo.getP4().equals(sha256Password) || userInfo.getP3().equals(sha256Password) ||
                userInfo.getP2().equals(sha256Password) || userInfo.getP1().equals(sha256Password)) {
            res.put("error", "新密码不能与近期使用过的密码相同");
            return res;
        }

        boolean matches1 = Pattern.matches(".*[a-z].*", newPasswordAfterRSADecrypt);
        boolean matches2 = Pattern.matches(".*[0-9].*", newPasswordAfterRSADecrypt);
        boolean matches3 = Pattern.matches(".*[#?!@$%^&*\\-].*", newPasswordAfterRSADecrypt);
        boolean matches4 = Pattern.matches("^.{8,20}$", newPasswordAfterRSADecrypt);

        //新密码不能有用户电话号码连续4位数字
        for(int i =0;i<7;i++){
            String substring = phoneAfterRSADecrypt.substring(i, i + 4);
            if(newPasswordAfterRSADecrypt.contains(substring)){
                res.put("error", "不能有用户电话号码连续4位数字");
                return res;
            }
        }
        if (!(matches1  && matches2 && matches4 && matches3 )) {
            res.put("error", "新密码不符合规则");
            return res;
        }

        loginMapper.updatePassword(SHA256Util.getSHA256Str(newPasswordAfterRSADecrypt + userInfo.getSalt()), "1", userInfo.getUid());
        res.put("success", "密码更新成功");
        if (++count == 6) {
            loginMapper.updateCountAndLastPassword(1, SHA256Util.getSHA256Str(newPasswordAfterRSADecrypt + userInfo.getSalt()), "p" + 5, userInfo.getUid());
        } else {
            loginMapper.updateCountAndLastPassword(count, SHA256Util.getSHA256Str(newPasswordAfterRSADecrypt + userInfo.getSalt()), "p" + --count, userInfo.getUid());
        }

        return res;
    }
}
