package com.whatsub.honeybread.worker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.whatsub.honeybread")
public class Worker {

    public static void main(String[] args) {
        SpringApplication.run(Worker.class, args);
    }
}
