package com.loco.transaction.management;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TransactionsManagementApplicationTests {

	@Test
	void contextLoads() {
		Assertions.assertDoesNotThrow(() -> {
			TransactionManagementApplication.main(new String[]{});
		});
	}

}
