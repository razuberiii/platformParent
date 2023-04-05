package com.platform.service.login.impl;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.platform.service.login.KaptchaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Encoder;
import com.platform.utils.RedisUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;


@Service
public class KaptchaServiceImpl implements KaptchaService {

    @Autowired
    DefaultKaptcha producer;

    @Autowired
    RedisUtil redisUtil;

    @Override
    public Map<String, Object> generateVerifyCode() throws Exception {

        // 生成文字验证码
        String text = producer.createText();
        // 生成图片验证码
        ByteArrayOutputStream outputStream = null;
        BufferedImage image = producer.createImage(text);

        outputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", outputStream);

        // 对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        Map<String, Object> map = new HashMap<>();
        String base64= encoder.encode(outputStream.toByteArray());
        String kaptchaBase64="data:image/jpeg;base64,"+base64.replaceAll("\r\n","");
        map.put("textCode", text);
        map.put("picCode", kaptchaBase64);
        //生成时间错，将时间戳放入map中返回，并存入Redis设置过期时间，进行后续验证
        long timestamp = System.currentTimeMillis();
        map.put("timestamp",timestamp);
//        picCodeMapper.insertPicCodeInfo(text,String.valueOf(timestamp));
        redisUtil.setForTimeMIN("picCode:"+String.valueOf(timestamp), text,15);

        return map;
    }
}