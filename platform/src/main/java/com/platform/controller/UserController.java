package com.platform.controller;

import com.platform.constants.Result;
import com.platform.constants.ResultCode;
import com.platform.dto.LoginDto;
import com.platform.service.login.*;
import com.yiliedu.platform.service.login.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import utils.RSAUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;


@RequestMapping(value = "/mp/internet/wechat")
@RestController
public class UserController {

    @Autowired
    LoginService loginService;

    @Autowired
    VerifyPicCodeService verifyPicCodeService;

    @Autowired
    KaptchaService kaptchaService;

    @Autowired
    SendCodeService sendCodeService;

    @Autowired
    PasswordChangeService passwordChangeService;


    @PostMapping("/login")
    public Result login(@RequestBody LoginDto loginDto, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> map = loginService.login(loginDto, request,response);
        if(!StringUtils.isEmpty(map.get("success"))){
            return new Result(ResultCode.SUCCESS,"登录成功",map);
        }
        return new Result(ResultCode.FIILED, (String) map.get("error"));
    }


    @GetMapping("/getPicCode")
    public Result getPicCode() throws Exception {
        return new Result(ResultCode.SUCCESS, "获取图形验证码成功", kaptchaService.generateVerifyCode());
    }

    @GetMapping("/getPublicKey")
    public Result getPublicKey() throws Exception {
        HashMap<String, Object> map = new HashMap<>();
        map.put("publicKey", RSAUtil.getPublicKey().getEncoded());

        return new Result(ResultCode.SUCCESS, "获取RSA公钥成功", map);
    }

    @PostMapping("/verifyPicCode")
    public Result verifyPicCode(@RequestBody LoginDto loginDto) {
       Boolean isSuccess = verifyPicCodeService.verifyPicCode(loginDto.getPicCode(),String.valueOf(loginDto.getTimestamp()));
       if(isSuccess){
           return new Result(ResultCode.SUCCESS,"验证成功");
       }
        return new Result(ResultCode.FIILED, "验证失败");
    }

    @PostMapping("/sendCode")
    public Result sendCode(@RequestBody LoginDto loginDto ) throws Exception {
        HashMap<String, Object> map = new HashMap<>();
        map.put("code","123456");
        return new Result(ResultCode.SUCCESS,"发送验证码成功",sendCodeService.sendCode(loginDto.getPhone()));
    }
    @PostMapping("/passwordChange")
    public Result passwordChange(@RequestBody LoginDto loginDto) throws Exception {
        Map<String, Object> map = passwordChangeService.passwordChangeService(loginDto);
        if(!StringUtils.isEmpty(map.get("success"))){
            return new Result(ResultCode.SUCCESS, (String) map.get("success"));
        }
        return new Result(ResultCode.FIILED, (String) map.get("error"));
    }

}