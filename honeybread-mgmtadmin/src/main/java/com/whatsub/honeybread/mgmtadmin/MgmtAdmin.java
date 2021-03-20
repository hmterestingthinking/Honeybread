package com.whatsub.honeybread.mgmtadmin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.whatsub.honeybread")
public class MgmtAdmin {

    public static void main(String[] args) {
        SpringApplication.run(MgmtAdmin.class, args);
    }
}
