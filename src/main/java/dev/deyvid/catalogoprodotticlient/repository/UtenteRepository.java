package dev.deyvid.catalogoprodotticlient.repository;

import org.springframework.data.repository.CrudRepository;

import dev.deyvid.catalogoprodotticlient.model.Utente;
public interface UtenteRepository extends CrudRepository<Utente, Integer>{
	
	public Utente getByUsername(String username);
}
