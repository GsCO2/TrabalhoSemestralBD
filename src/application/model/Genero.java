package application.model;

public class Genero {
	private int id;
	private String nome;
	
	public Genero() {
		super();
	}
	
	public Genero(int id, String nome) {
		this.id = id;
		this.nome = nome;
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
	
	@Override
	public String toString() {
		return nome;
	}
	
	@Override
	public boolean equals(Object o) {
	    if (this == o) {
	    	return true;
	    }
	    if (o == null || getClass() != o.getClass()) {
	    	return false;
	    }
	    Genero genero = (Genero) o;
	    return id == genero.id;
	}

	@Override
	public int hashCode() {
	    return Integer.hashCode(id);
	}
}
