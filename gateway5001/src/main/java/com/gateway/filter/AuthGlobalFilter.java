package com.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.gateway.entity.Whitelist;
import com.gateway.utils.RedisUtil;
import entity.Result;
import entity.ResultCode;
import entity.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import utils.RSAUtil;

import java.nio.charset.StandardCharsets;
import java.util.HashSet;


@Component
public class AuthGlobalFilter implements GlobalFilter, Ordered {
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    Whitelist whitelist;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        HttpCookie tokenJson = request.getCookies().getFirst("token");
        String currentUrl = request.getURI().getPath();
        //登录接口等不做登录验证直接放行
        HashSet<String> whitelist = this.whitelist.getWhitelist();
        if(whitelist.contains(currentUrl)){
            return chain.filter(exchange);
        }
        if (tokenJson != null) {
            try {
                String decrypt = RSAUtil.decrypt(tokenJson.getValue(), RSAUtil.getPrivateKey());
                Token token = JSON.parseObject(decrypt, Token.class);
                String uid = token.getUid();
                String tokenIPFromRedis = redisUtil.get("token" + uid);
                if (!StringUtils.isEmpty(tokenIPFromRedis)) {
                    String ipAddress = getIpAddress(request);
                    if (tokenIPFromRedis.equals(ipAddress)) {
                        redisUtil.setForTimeMIN("token" + uid,ipAddress,15);
                        return chain.filter(exchange);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        String result = JSON.toJSONString(new Result(ResultCode.FIILED, "登录失效，请重新登录"));
        byte[] bytes = result.getBytes(StandardCharsets.UTF_8);
        DataBuffer wrap = response.bufferFactory().wrap(bytes);
        response.getHeaders().add("Content-type","application/json;charset=UTF-8");
        return response.writeWith(Mono.just(wrap));
    }

    @Override
    public int getOrder() {
        return 0;
    }

    private static String getIpAddress(ServerHttpRequest request) {
        HttpHeaders headers = request.getHeaders();
        String ip = headers.getFirst("x-forwarded-for");
        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
            if (ip.indexOf(",") != -1) {
                ip = ip.split(",")[0];
            }
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = headers.getFirst("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = headers.getFirst("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = headers.getFirst("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = headers.getFirst("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = headers.getFirst("X-Real-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddress().getAddress().getHostAddress();
        }
        return ip;
    }
}
