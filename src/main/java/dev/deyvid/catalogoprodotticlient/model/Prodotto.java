package dev.deyvid.catalogoprodotticlient.model;

public class Prodotto {
	private Integer id;
	private String nome;
	private Float prezzo;
	
	public Prodotto() {
		super();
	}
	
	public Prodotto(String nome, Float prezzo) {
		super();
		this.nome = nome;
		this.prezzo = prezzo;
	}
	
	@Override
	public String toString() {
		return "Prodotto [id=" + id + ", nome=" + nome + ", prezzo=" + prezzo + "]";
	}
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public Float getPrezzo() {
		return prezzo;
	}
	
	public void setPrezzo(Float prezzo) {
		this.prezzo = prezzo;
	}
	
	
	
}
