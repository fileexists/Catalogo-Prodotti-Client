package dev.deyvid.catalogoprodotticlient.repository;

import org.springframework.data.repository.CrudRepository;

import dev.deyvid.catalogoprodotticlient.model.Ruolo;

public interface RuoloRepository extends CrudRepository<Ruolo, Integer>{
	public Ruolo getByNome(String nome);

}
