package com.nwt.juber;

import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;

@SpringBootTest
@PropertySource("classpath:application-test.yml")
class JuberApplicationTests {

	@Test
	void contextLoads() {
	}

}
