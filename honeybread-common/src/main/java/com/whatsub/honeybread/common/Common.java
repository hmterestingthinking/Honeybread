package com.whatsub.honeybread.common;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.whatsub.honeybread")
public class Common {

    public static void main(String[] args) {
        SpringApplication.run(Common.class, args);
    }
}
