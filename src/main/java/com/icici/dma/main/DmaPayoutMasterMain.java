package com.icici.dma.main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.icici.dma")

@PropertySources({

		@PropertySource("classpath:application.properties"),

})
@EnableJpaRepositories(basePackages = { "com.icici.dma.repository", "com.icici.dma.slabRepository", "com.icici.dma.configMasterRepository"} )
@EntityScan(basePackages = { "com.icici.dma.model", "com.icici.dma.slabEntity"} )
public class DmaPayoutMasterMain extends SpringBootServletInitializer {
	
	private static final Logger logger = LogManager.getLogger(DmaPayoutMasterMain.class);

	public static void main(String[] args) {
		
		//logger.info("====== MAIN CLASS LOG WORKING ========");

		SpringApplication.run(DmaPayoutMasterMain.class, args);

		System.out.println("====== DMA PAYOUT MASTER SERVICE STARTED SUCCESSFULLY ========");
	}

}
