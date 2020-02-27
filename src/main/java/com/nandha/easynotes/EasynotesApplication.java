package com.nandha.easynotes;

import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableProcessApplication
@EnableEurekaClient
@EnableJpaAuditing
@EnableCircuitBreaker
@EnableHystrixDashboard
@EnableCaching
public class EasynotesApplication {

	public static void main(String[] args) {
		SpringApplication.run(EasynotesApplication.class, args).start();
	}

}
