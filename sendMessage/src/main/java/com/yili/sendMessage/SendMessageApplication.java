package com.yili.sendMessage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class SendMessageApplication {
   public static void main(String[] args) {
       try {
           SpringApplication.run(SendMessageApplication.class, args);
       }catch (Exception e) {
            e.printStackTrace();
       }

    }

}
