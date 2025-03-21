package dev.deyvid.catalogoprodotticlient.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class CatalogoClient {
	@Autowired
	private RestTemplate template;
}
