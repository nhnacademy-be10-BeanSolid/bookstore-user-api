package com.nhnacademy.bookstoreuserapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class BookstoreUserApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookstoreUserApiApplication.class, args);
	}

}
