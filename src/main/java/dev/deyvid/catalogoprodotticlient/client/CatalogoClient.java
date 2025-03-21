package dev.deyvid.catalogoprodotticlient.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import dev.deyvid.catalogoprodotticlient.model.Prodotto;
import dev.deyvid.catalogoprodotticlient.model.Utente;

@Component
public class CatalogoClient {
	
	@Autowired
	private RestTemplate restTemplate;
	
	private final static String URL = "http://localhost:8080/v1/prodotti";
	
	public HttpHeaders getHeaders(Utente u) {
		HttpHeaders headers =new HttpHeaders();
        headers.set("username", u.getUsername());
        headers.set("password", u.getPassword());
        return headers;
	}
	
	public ResponseEntity<List<Prodotto>> ricercaProdotti(Utente utente, String nome, Float prezzo) {
		try {
	        String url = URL + "/getByNomeAndPrezzo/" + nome + "/" + prezzo;
	        HttpHeaders headers = getHeaders(utente);
	        HttpEntity<String> entity = new HttpEntity<>(headers);
	        ResponseEntity<Prodotto[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, Prodotto[].class);
	        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
	            return new ResponseEntity<>(Arrays.asList(response.getBody()), HttpStatus.OK);
	        } else {
	            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	        }
        	
	    } catch (HttpClientErrorException.BadRequest e) {
	        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	    } catch (HttpClientErrorException.Unauthorized e) {
	        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	    }catch (HttpClientErrorException.Forbidden e) {
	        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
	    } catch (HttpServerErrorException.InternalServerError e) {
	        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);        
	    } catch (HttpClientErrorException.NotFound e) {
	        return new ResponseEntity<>(HttpStatus.NOT_FOUND);   	        
	    } catch (Exception e) {
	        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	    }
    }
    
    public ResponseEntity<Void> creaProdotto(Utente utente, Prodotto prodotto) {
    	try {
        String url = URL + "/insert";
        HttpHeaders headers = getHeaders(utente);
        HttpEntity<Prodotto> entity = new HttpEntity<Prodotto>(prodotto, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        HttpStatusCode statusCode = response.getStatusCode();

        return new ResponseEntity<>(statusCode);
	        
	    } catch (HttpClientErrorException.BadRequest e) {
	        return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
	    } catch (HttpClientErrorException.Unauthorized e) {
	        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	    }catch (HttpClientErrorException.Forbidden e) {
	        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
	    } catch (HttpServerErrorException.InternalServerError e) {
	        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	    } catch (Exception e) {
	        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}
    
    public ResponseEntity<Void> totalUpdate(Utente utente, Integer id, String nome, Float prezzo) {
        try {
            String url = URL + "/totalUpdate";

            Prodotto prodotto = new Prodotto();
            prodotto.setId(id);
            prodotto.setNome(nome);
            prodotto.setPrezzo(prezzo);

            HttpHeaders headers = getHeaders(utente);
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Prodotto> entity = new HttpEntity<>(prodotto, headers);

            ResponseEntity<Prodotto> response = restTemplate.exchange(url, HttpMethod.PUT, entity, Prodotto.class);

            HttpStatusCode statusCode = response.getStatusCode();

            if (statusCode == HttpStatus.CREATED) {
                return new ResponseEntity<>(HttpStatus.CREATED);
            } else if (statusCode == HttpStatus.NOT_FOUND) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            
        } catch (HttpClientErrorException.BadRequest e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (HttpClientErrorException.Unauthorized e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (HttpClientErrorException.Forbidden e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } catch (HttpServerErrorException.InternalServerError e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    public ResponseEntity<Void> partialUpdate(Utente utente, Integer id, String nome, Float prezzo) {
        try {
            String url = URL + "/partialUpdate";
            Prodotto prodotto = new Prodotto();
            prodotto.setId(id);
            if (nome != null && !nome.isEmpty()) {
                prodotto.setNome(nome);
            }
            if (prezzo != null) {
                prodotto.setPrezzo(prezzo);
            }
            HttpHeaders headers = getHeaders(utente);
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Prodotto> entity = new HttpEntity<>(prodotto, headers);
            ResponseEntity<Prodotto> response = restTemplate.exchange(url, HttpMethod.PATCH, entity, Prodotto.class);
            HttpStatusCode statusCode = response.getStatusCode();
            
            if (statusCode == HttpStatus.CREATED) {
                return new ResponseEntity<>(HttpStatus.CREATED);
            } else if (statusCode == HttpStatus.NOT_FOUND) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }

        } catch (HttpClientErrorException.BadRequest e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (HttpClientErrorException.Unauthorized e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (HttpClientErrorException.Forbidden e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } catch (HttpServerErrorException.InternalServerError e) {
        	e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
        	e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    public ResponseEntity<Void> delete(Utente utente, Integer id) { 
        try {
            String url = URL + "/deleteById/" + id;
            HttpHeaders headers = getHeaders(utente);

            HttpEntity<Void> entity = new HttpEntity<>(headers);
            ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.DELETE, entity, Void.class);
            HttpStatusCode statusCode = response.getStatusCode();

            if (statusCode == HttpStatus.NO_CONTENT) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else if (statusCode == HttpStatus.NOT_FOUND) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }

        } catch (HttpClientErrorException.BadRequest e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (HttpClientErrorException.Unauthorized e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (HttpClientErrorException.Forbidden e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } catch (HttpServerErrorException.InternalServerError e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
