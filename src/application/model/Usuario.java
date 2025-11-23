package application.model;

import java.time.LocalDate;

public class Usuario {
	private int id;
	private String nome;
	private String username;
	private String email;
	private String senha;
	private LocalDate dataCadastro;
	private String numCelular;
	
	public Usuario() {
		super();
	}
	
	public Usuario(int id, String nome, String username, String email, 
					String senha, LocalDate dataCadastro, String numCelular) {
		this.id = id;
		this.nome = nome;
		this.username = username;
		this.email = email;
		this.senha = senha;
		this.dataCadastro = dataCadastro;
		this.numCelular = numCelular;
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
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getSenha() {
		return senha;
	}
	
	public void setSenha(String senha) {
		this.senha = senha;
	}
	
	public LocalDate getData() {
		return dataCadastro;
	}
	
	public void setData(LocalDate data) {
		this.dataCadastro = data;
	}
	
	public String getNumCelular() {
		return numCelular;
	}
	
	public void setNumCelular(String numCelular) {
		this.numCelular = numCelular;
	}
	
	@Override
	public String toString() {
		return nome;
	}
}
	
	