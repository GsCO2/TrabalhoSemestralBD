package application.model;

public class Empresa {
	private int id;
	private String nome;
	private String pais;
	private String tipo;
	
	public Empresa() {
		super();
	}
	public Empresa(int id, String nome, String pais, String tipo) {
		this.id = id;
		this.nome = nome;
		this.pais = pais;
		this.tipo = tipo;
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
	
	public String getPais() {
		return pais;
	}
	
	public void setPais(String pais) {
		this.pais = pais;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	@Override
	public String toString() {
		return nome;
	}
}
