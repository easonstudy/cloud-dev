package com.lottery.center;

import com.lottery.center.socket.ServerDemo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CenterApplication {

	public static void main(String[] args) {
		//启动Socket Server

		SpringApplication.run(CenterApplication.class, args);

        //socket 启动
        ServerDemo.start();
	}



}
