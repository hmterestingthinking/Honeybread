package com.whatsub.honeybread.channel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.whatsub.honeybread")
public class Channel {

    public static void main(String[] args) {
        SpringApplication.run(Channel.class, args);
    }
}
