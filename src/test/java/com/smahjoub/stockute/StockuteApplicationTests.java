package com.smahjoub.stockute;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations= "classpath:application-test.yaml")
class StockuteApplicationTests {

	@Test
	void contextLoads() {
	}

}
