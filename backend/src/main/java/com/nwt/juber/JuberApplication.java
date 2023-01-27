package com.nwt.juber;

import com.nwt.juber.config.AppProperties;
import com.nwt.juber.service.FileStorageService;
import com.nwt.juber.service.TimeEstimator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.PostConstruct;
import java.io.IOException;

@SpringBootApplication
@EnableScheduling
@EnableAsync
@EnableTransactionManagement
@EnableConfigurationProperties(AppProperties.class)
public class JuberApplication {


	public static void main(String[] args) {
		SpringApplication.run(JuberApplication.class, args);
	}

	@Bean
	CommandLineRunner init(FileStorageService storageService) {
		return args -> {
			storageService.init();
		};
	}

}
