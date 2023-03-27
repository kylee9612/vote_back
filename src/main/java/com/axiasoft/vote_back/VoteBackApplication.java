package com.axiasoft.vote_back;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.axiasoft.vote_back")
public class VoteBackApplication {

	public static void main(String[] args) {
		SpringApplication.run(VoteBackApplication.class, args);
	}

}
