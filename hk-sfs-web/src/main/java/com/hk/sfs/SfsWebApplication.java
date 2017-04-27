package com.hk.sfs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class SfsWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(SfsWebApplication.class, args);
    }
}
