package com.msc.ms.users;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MscMsUsersApplication {

	public static void main(String[] args) {
		SpringApplication.run(MscMsUsersApplication.class, args);
	}

}
