package uk.co.michaelbannister;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.jetty.JettyEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;

import java.lang.management.ManagementFactory;

@SpringBootApplication
public class SpringSiteApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringSiteApplication.class, args);
	}

}
