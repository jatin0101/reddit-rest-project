package com.jg.redditjavaproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@ComponentScan(basePackages = {"com.jg.redditjavaproject"})
public class RedditJavaProjectApplication {

	public static void main(String[] args) {

		try {
			SpringApplication.run(RedditJavaProjectApplication.class, args);
		} catch(Exception e){
			System.out.println(e.toString());
		}
	}
}
