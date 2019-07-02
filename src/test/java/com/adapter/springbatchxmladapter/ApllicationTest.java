package com.adapter.springbatchxmladapter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class ApllicationTest {
	
	@Test
	public void applicationContextTest() {
		Application.main(new String[] {});
	}
}
