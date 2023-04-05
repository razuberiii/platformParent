package com.gateway.entity;

import org.springframework.stereotype.Component;

import java.util.HashSet;
@Component
public class Whitelist {
    public HashSet<String> getWhitelist() {
        return whitelist;
    }

    HashSet<String> whitelist = new HashSet<>();
    {
        whitelist.add( "/mp/internet/wechat/getPublicKey");
        whitelist.add( "/mp/internet/wechat/sendCode");
        whitelist.add( "/mp/internet/wechat/getPicCode");
        whitelist.add( "/mp/internet/wechat/verifyPicCode");
        whitelist.add( "/mp/internet/wechat/login");
    }
}
