package com.deviceinsight.spring.sample;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.SocketUtils;

import java.time.Duration;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class GatewaySampleApplicationTests {

	@LocalServerPort
	protected int port = 0;

	protected static int managementPort;

	protected WebTestClient webClient;
	protected String baseUri;

	@BeforeClass
	public static void beforeClass() {
		managementPort = SocketUtils.findAvailableTcpPort();

		System.setProperty("management.server.port", String.valueOf(managementPort));
	}

	@AfterClass
	public static void afterClass() {
		System.clearProperty("management.server.port");
	}

	@Before
	public void setup() {
		baseUri = "http://localhost:" + port;
		this.webClient = WebTestClient.bindToServer().responseTimeout(Duration.ofSeconds(10)).baseUrl(baseUri).build();
	}

	@Test
	public void testWithoutPercentage() {
		webClient.get().uri("/anything/100").exchange().expectStatus().isOk();
	}

	@Test
	public void testWithPercentage() {
		webClient.get().uri("/anything/100%").exchange().expectStatus().isOk();
	}

}
