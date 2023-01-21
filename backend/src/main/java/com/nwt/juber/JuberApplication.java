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

import javax.annotation.PostConstruct;
import java.io.IOException;

@SpringBootApplication
@EnableScheduling
@EnableAsync
@EnableConfigurationProperties(AppProperties.class)
public class JuberApplication {

	@Autowired
	TimeEstimator timeEstimator;

	public static void main(String[] args) {
		SpringApplication.run(JuberApplication.class, args);
	}

	@Bean
	CommandLineRunner init(FileStorageService storageService) {
		return args -> {
			storageService.init();
		};
	}

	@PostConstruct
	public void init() throws IOException {
		// 45.24233695159381, 19.843704724580878
		// 45.26072683505999, 19.846246988642296
		int secs = timeEstimator.estimateTime(45.24233695159381,19.843704724580878,45.26072683505999,19.846246988642296);
		System.out.println(secs);
	}

}
