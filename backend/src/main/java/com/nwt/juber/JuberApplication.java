package com.nwt.juber;

import com.nwt.juber.config.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties(AppProperties.class)
public class JuberApplication {

	public static void main(String[] args) {
		SpringApplication.run(JuberApplication.class, args);
	}

}
