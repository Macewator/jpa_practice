package com.example;

import com.example.Controller.AppController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.Scanner;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(Main.class,args);

        AppController controller = ctx.getBean(AppController.class);
        controller.mainLoop();
    }

    @Bean
    public Scanner getScanner(){
        return new Scanner(System.in);
    }
}
