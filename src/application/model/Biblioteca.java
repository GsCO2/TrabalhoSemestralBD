package application.model;

import java.time.LocalDate;

public class Biblioteca {
	private Usuario usuario;
	private Jogo jogo;
	private LocalDate dataCompra;
	private int horasJogadas;
	
	public Biblioteca() {
		super();
	}
	
	public Biblioteca(Usuario usuario, Jogo jogo, LocalDate dataCompra, int horasJogadas) {
		this.usuario = usuario;
		this.jogo = jogo;
		this.dataCompra = dataCompra;
		this.horasJogadas = horasJogadas;
	}

	public Usuario getUsuario() {
		return usuario;
	}


	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Jogo getJogo() {
		return jogo;
	}

	public void setJogo(Jogo jogo) {
		this.jogo = jogo;
	}

	public LocalDate getDataCompra() {
		return dataCompra;
	}

	public void setDataCompra(LocalDate dataCompra) {
		this.dataCompra = dataCompra;
	}

	public int getHorasJogadas() {
		return horasJogadas;
	}

	public void setHorasJogadas(int horasJogadas) {
		this.horasJogadas = horasJogadas;
	}
	
	@Override
	public String toString() {
		return usuario.getNome()
				+ " - " + jogo.getNome();
	}
}
