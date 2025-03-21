package dev.deyvid.catalogoprodotticlient.ui;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.client.RestTemplate;

import dev.deyvid.catalogoprodotticlient.service.UtentiService;

@SpringBootApplication( scanBasePackages = { "dev.deyvid.catalogoprodotticlient.service", "dev.deyvid.catalogoprodotticlient.controller", "dev.deyvid.catalogoprodotticlient.client" })
@EnableJpaRepositories( basePackages = { "dev.deyvid.catalogoprodotticlient.repository" })
@EntityScan( basePackages = { "dev.deyvid.catalogoprodotticlient.model" })
public class CatalogoProdottiClientApplication {

	public static void main(String[] args) {
		try {
			ConfigurableApplicationContext applicationContext = SpringApplication.run(CatalogoProdottiClientApplication.class, args);
			
			UtentiService utentiService = applicationContext.getBean("utentiService", UtentiService.class);
			utentiService.createRolesIfNotPresent();
			utentiService.createUsersIfNotPresent();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	@Bean
	public RestTemplate template() {
		return new RestTemplate();
	}

}
