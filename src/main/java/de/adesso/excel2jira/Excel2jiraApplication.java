package de.adesso.excel2jira;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class Excel2jiraApplication {

	public static void main(String[] args) {
		SpringApplication.run(Excel2jiraApplication.class, args);
	}

}
