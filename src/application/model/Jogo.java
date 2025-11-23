package application.model;

import java.time.LocalDate;
import java.util.List;

public class Jogo {
	private int id;
	private String nome;
	private Double preco;
	private LocalDate dataLancamento;
	private Empresa publicadora;
	private Empresa desenvolvedora;
	private List<Genero> generos;
	
	public Jogo() {
		
	}
	public Jogo(int id, String nome, Double preco, LocalDate dataLancamento, 
			Empresa publicadora, Empresa desenvolvedora, List<Genero> generos) {
		this.id = id;
		this.nome = nome;
		this.preco = preco;
		this.dataLancamento = dataLancamento;
		this.publicadora = publicadora;
		this.desenvolvedora = desenvolvedora;
		this.generos = generos;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Double getPreco() {
		return preco;
	}

	public void setPreco(Double preco) {
		this.preco = preco;
	}

	public LocalDate getDataLancamento() {
		return dataLancamento;
	}

	public void setDataLancamento(LocalDate dataLancamento) {
		this.dataLancamento = dataLancamento;
	}

	public Empresa getPublicadora() {
		return publicadora;
	}

	public void setPublicadora(Empresa publicadora) {
		this.publicadora = publicadora;
	}

	public Empresa getDesenvolvedora() {
		return desenvolvedora;
	}

	public void setDesenvolvedora(Empresa desenvolvedora) {
		this.desenvolvedora = desenvolvedora;
	}

	public List<Genero> getGeneros() {
		return generos;
	}

	public void setGeneros(List<Genero> generos) {
		this.generos = generos;
	}
	
	@Override
	public String toString() {
		return nome;
	}
}
