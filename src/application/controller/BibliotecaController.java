package application.controller;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import application.model.Biblioteca;
import application.model.Jogo;
import application.model.Usuario;
import application.persistence.BibliotecaDao;
import application.persistence.GenericDao;
import application.persistence.JogoDao;
import application.persistence.UsuarioDao;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class BibliotecaController implements EventHandler<Event> {
	private ComboBox <Usuario> cBUsuarioBib;
	private ComboBox <Jogo> cBJogoBib;
	private TextArea tABib;
	private DatePicker tFDataBib;
	private TextField tFHoraBib;
	
	public BibliotecaController(ComboBox<Usuario> cBUsuarioBib, ComboBox<Jogo> cBJogoBib, TextArea tABib,
			DatePicker tFDataBib, TextField tFHoraBib) {
		this.cBUsuarioBib = cBUsuarioBib;
		this.cBJogoBib = cBJogoBib;
		this.tABib = tABib;
		this.tFDataBib = tFDataBib;
		this.tFHoraBib = tFHoraBib;
	}

	@Override
	public void handle(Event ev) {
		String inp = ev.getSource().toString();
		
		if(inp.contains("Inserir") || inp.contains("Alterar")) {
			if(cBUsuarioBib.getValue() == null) {
				mostraMensagem("Usuário Inválido.");
				return;
			}
			if(cBJogoBib.getValue() == null) {
				mostraMensagem("Jogo Inválido.");
				return;
			}
			if(tFDataBib.getValue().isAfter(LocalDate.now())) {
				mostraMensagem("Data de Compra Inválida.");
				return;
			}
			if(Integer.parseInt(tFHoraBib.getText()) < 0) {
				mostraMensagem("número de horas Inválida.");
			}
		}
		
		
		if(inp.contains("Excluir") || inp.contains("Alterar")) {
			if(cBUsuarioBib.getValue() == null || cBJogoBib.getValue() == null) {
				mostraMensagem("Usuário ou Jogo inválido, selecione novamente.");
				return;
			}
		}
		
		if(inp.contains("Listar") || inp.contains("Total")) {
			if(cBUsuarioBib.getValue() == null) {
				mostraMensagem("Usuário Inválido");
				return;
			}
		}
		
		if(inp.contains("Pesquisar")) {
			if(cBJogoBib.getValue() == null) {
				mostraMensagem("Jogo Inválido");
				return;
			}
		}
		
		try {
			if(inp.contains("Inserir")) {
				inserirBiblioteca();
			} else if(inp.contains("Excluir")) {
				excluirBiblioteca();
			} else if(inp.contains("Alterar")) {
				alterarBiblioteca();
			} else if(inp.contains("Listar")) {
				listarBiblioteca();
			} else if(inp.contains("Pesquisar")) {
				pesquisarBiblioteca();
			} else if(inp.contains("Filtrar")) {
				filtrarBiblioteca(); 
			} else if(inp.contains("Total")) {
				totalBiblioteca();
			}
		} catch (SQLException | ClassNotFoundException e) {
			e.getMessage();
		}
		
	}
	
	private void inserirBiblioteca() throws ClassNotFoundException, SQLException {
		GenericDao gDao = new GenericDao();
		BibliotecaDao bDao = new BibliotecaDao(gDao);
		Usuario u = new Usuario();
		Jogo j = new Jogo();
		Biblioteca b = new Biblioteca();
		u.setId(cBUsuarioBib.getValue().getId());
		j.setId(cBJogoBib.getValue().getId());
		b.setJogo(j);
		b.setUsuario(u);
		b.setHorasJogadas(Integer.parseInt(tFHoraBib.getText()));
		b.setDataCompra(tFDataBib.getValue());
		bDao.inserir(b);
		mostraMensagem("Biblioteca inserida.");
		atualizarComboIds();
		limparCampos();
	}

	private void excluirBiblioteca() throws ClassNotFoundException, SQLException {
		GenericDao gDao = new GenericDao();
		BibliotecaDao bDao = new BibliotecaDao(gDao);
		Usuario u = new Usuario();
		Jogo j = new Jogo();
		Biblioteca b = new Biblioteca();
		u.setId(cBUsuarioBib.getValue().getId());
		j.setId(cBJogoBib.getValue().getId());
		b.setJogo(j);
		b.setUsuario(u);
		bDao.excluir(b);
		mostraMensagem("Biblioteca excluida.");
		atualizarComboIds();
		limparCampos();
	}

	private void alterarBiblioteca() throws ClassNotFoundException, SQLException {
		GenericDao gDao = new GenericDao();
		BibliotecaDao bDao = new BibliotecaDao(gDao);
		Usuario u = new Usuario();
		Jogo j = new Jogo();
		Biblioteca b = new Biblioteca();
		u.setId(cBUsuarioBib.getValue().getId());
		j.setId(cBJogoBib.getValue().getId());
		b.setJogo(j);
		b.setUsuario(u);
		b.setHorasJogadas(Integer.parseInt(tFHoraBib.getText()));
		b.setDataCompra(tFDataBib.getValue());
		bDao.alterar(b);
		mostraMensagem("Biblioteca alterada.");
		atualizarComboIds();
		limparCampos();
	}

	private void listarBiblioteca() throws ClassNotFoundException, SQLException {
		tABib.clear();
		GenericDao gDao = new GenericDao();
		BibliotecaDao bDao = new BibliotecaDao(gDao);
		Usuario u = new Usuario();
		Biblioteca b = new Biblioteca();
		u.setId(cBUsuarioBib.getValue().getId());
		b.setUsuario(u);
		List <Biblioteca> biblios= bDao.filtrarPorUsuario(b);
		StringBuffer buffer = new StringBuffer();
		for(Biblioteca bi : biblios) {
			buffer.append(bi.getJogo().getNome() + " - ");
			buffer.append(bi.getHorasJogadas() + " - ");
			buffer.append(bi.getUsuario().getUsername());
			buffer.append("\n");
		}
		tABib.setText(buffer.toString());
	}

	private void pesquisarBiblioteca() throws ClassNotFoundException, SQLException {
		tABib.clear();
		GenericDao gDao = new GenericDao();
		BibliotecaDao bDao = new BibliotecaDao(gDao);
		Jogo j = new Jogo();
		Biblioteca b = new Biblioteca();
		j.setId(cBJogoBib.getValue().getId());
		b.setJogo(j);
		List <Biblioteca> biblios = bDao.listarPorJogo(b);
		StringBuffer buffer = new StringBuffer();
		for(Biblioteca bi : biblios) {
			buffer.append(bi.getUsuario().getUsername() + " - ");
			buffer.append(bi.getHorasJogadas() + " - ");
			buffer.append(bi.getJogo().getNome()) ;
			buffer.append("\n");
		}
		tABib.setText(buffer.toString());
	}

	private void filtrarBiblioteca() throws ClassNotFoundException, SQLException {
		tABib.clear();
		GenericDao gDao = new GenericDao();
		BibliotecaDao bDao = new BibliotecaDao(gDao);
		List <Biblioteca> biblios = bDao.jogosCompradosDtLan();
		StringBuffer buffer = new StringBuffer();
		for(Biblioteca bi : biblios) {
			buffer.append(bi.getUsuario().getUsername() + " - ");
			buffer.append(bi.getJogo().getNome() + " - ");
			buffer.append(bi.getJogo().getDataLancamento() + " - ");
			buffer.append(bi.getDataCompra());
			buffer.append("\n");
		}
		tABib.setText(buffer.toString());
	}
	
	private void totalBiblioteca() throws ClassNotFoundException, SQLException {
		GenericDao gDao = new GenericDao();
		BibliotecaDao bDao = new BibliotecaDao(gDao);
		Usuario u = new Usuario();
		Biblioteca b = new Biblioteca();
		u.setId(cBUsuarioBib.getValue().getId());
		b.setUsuario(u);
		int total = bDao.totalJogosUsuario(b);
		mostraMensagem("Total jogos do usuario " + cBUsuarioBib.getValue().getUsername() + ": " + total);
		limparCampos();
	}
	public void atualizarComboIds() throws ClassNotFoundException, SQLException {
		GenericDao gDao = new GenericDao();
		UsuarioDao uDao = new UsuarioDao(gDao);
	    JogoDao jDao = new JogoDao(gDao);
		cBUsuarioBib.getItems().clear();
		cBJogoBib.getItems().clear();
	    cBUsuarioBib.getItems().setAll(uDao.listar());
	    cBJogoBib.getItems().setAll(jDao.listar());
	}
	
	private void limparCampos() {
		cBJogoBib.setValue(null);
		cBUsuarioBib.setValue(null);
		tFDataBib.setValue(LocalDate.now());
		tFHoraBib.clear();
		tABib.clear();
	}
	
	private void mostraMensagem(String txt) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Aviso");
		alert.setHeaderText(null);
		alert.setContentText(txt);
		alert.showAndWait();
	}
	
}
