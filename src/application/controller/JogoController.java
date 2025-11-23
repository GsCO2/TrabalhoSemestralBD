package application.controller;



import javafx.event.EventHandler;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import application.model.Empresa;
import application.model.Genero;
import application.model.Jogo;
import application.persistence.GenericDao;
import application.persistence.JogoDao;
import javafx.beans.property.BooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.event.Event;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class JogoController implements EventHandler<Event> {
	
	
	private TextField tFIdJogo;
	private TextField tFNomeJogo;
	private TextField tFPrecoJogo;
	private DatePicker tFDataJogo;
	private TextArea tAJogo;
	private ComboBox<Empresa>cBPublicadoraJogo;
	private ComboBox<Empresa>cBDesenvolvedoraJogo;
	@SuppressWarnings("unused")
	private ListView<Genero> lVGeneroJogo;
	private ComboBox<Integer> cBIdJogo;
	private ObservableMap<Genero, BooleanProperty> estados = FXCollections.observableHashMap();

	public JogoController(TextField tFIdJogo, TextField tFNomeJogo, TextField tFPrecoJogo, DatePicker tFDataJogo,
			TextArea tAJogo, ComboBox<Empresa> cBPublicadoraJogo, ComboBox<Empresa> cBDesenvolvedoraJogo,
			ListView<Genero> lVGeneroJogo, ComboBox<Integer> cBIdJogo, ObservableMap<Genero, BooleanProperty> estados) {
		this.tFIdJogo = tFIdJogo;
		this.tFNomeJogo = tFNomeJogo;
		this.tFPrecoJogo = tFPrecoJogo;
		this.tFDataJogo = tFDataJogo;
		this.tAJogo = tAJogo;
		this.cBPublicadoraJogo = cBPublicadoraJogo;
		this.cBDesenvolvedoraJogo = cBDesenvolvedoraJogo;
		this.lVGeneroJogo = lVGeneroJogo;
		this.cBIdJogo = cBIdJogo;
		this.estados = estados;
	}

	@Override
	public void handle(Event ev) {
		String inp = ev.getSource().toString();
		
		if(inp.contains("Inserir") || inp.contains("Alterar")) {
			if (tFNomeJogo.getText().isBlank() ||
			    tFNomeJogo.getText().length() < 2) {
				mostraMensagem("Nome do jogo inválido.");
			    return;
			}
			if(tFPrecoJogo.getText().isBlank() ||
				Double.parseDouble(tFPrecoJogo.getText()) < 0) {
				mostraMensagem("Preco do jogo inválido.");
				return;
			}
			if(tFDataJogo.getValue().isAfter(LocalDate.now())) {
				mostraMensagem("Data de Lançamento Inválida.");
				return;
			}
			
			if(cBPublicadoraJogo.getValue() == null) {
				mostraMensagem("Selecione uma publicadora.");
				return;
			}
			
			if(cBDesenvolvedoraJogo.getValue() == null) {
				mostraMensagem("Selecione uma desenvolvedora.");
				return;																													
			}
			
			boolean existeGenero = estados.values().stream().anyMatch(BooleanProperty::get);
			if (!existeGenero) {
			    mostraMensagem("Selecione ao menos um gênero.");
			    return;
			}
		}
		
		if(!inp.contains("Inserir") && !inp.contains("Listar") && !inp.contains("Filtrar")) {
			if(cBIdJogo.getValue() == null) {
				mostraMensagem("ID não selecionado, selecione um ID.");
				return;
			}
		}
			
		if(inp.contains("Inserir") || inp.contains("Alterar")) {
			boolean exG = estados.values().stream().anyMatch(BooleanProperty::get);
			if(!exG) {
				mostraMensagem("Selecione ao menos 1 gênero");
				return;
			}
		}
		
		try {
			if(inp.contains("Inserir")) {
				inserirJogo();
			} else if(inp.contains("Excluir")) {
				excluirJogo();
			} else if(inp.contains("Alterar")) {
				alterarJogo();
			} else if(inp.contains("Listar")) {
				listarJogo();
			} else if(inp.contains("Pesquisar")) {
				pesquisarJogo();
			} else if(inp.contains("Filtrar")) {
				filtrarJogo();
			}
		} catch (SQLException | ClassNotFoundException e) {
			e.getMessage();
		}
		
	}
	
	private void inserirJogo() throws ClassNotFoundException, SQLException {
		GenericDao gDao = new GenericDao();
		JogoDao jDao = new JogoDao(gDao);
		Jogo j = new Jogo();
		j.setNome(tFNomeJogo.getText().trim());
        j.setPreco(Double.parseDouble(tFPrecoJogo.getText().trim()));
        j.setDataLancamento(tFDataJogo.getValue());
        j.setPublicadora(cBPublicadoraJogo.getValue());
        j.setDesenvolvedora(cBDesenvolvedoraJogo.getValue());
        List<Genero> generos = new ArrayList<>();
        for (var entry : estados.entrySet()) {
            if (entry.getValue().get()) {
                generos.add(entry.getKey());
            }
        }
        j.setGeneros(generos);
        jDao.inserir(j);
		mostraMensagem("Jogo inserido.");
		atualizarComboIds();
		limparCampos();
	}
	
	private void excluirJogo() throws ClassNotFoundException, SQLException {
		GenericDao gDao = new GenericDao();
		JogoDao jDao = new JogoDao(gDao);
		Jogo j = new Jogo();
		j.setId(cBIdJogo.getValue());
		if(jDao.estaEmUso(j) == true) {
			Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
			alert.setTitle("Jogo possui biblioteca");
			alert.setHeaderText(null);
			alert.setContentText("Este jogo existe em diversas bibliotecas. O jogo será excluído de todas a bibliotecas.\n"
					+ "Deseja excluir mesmo assim?");
			Optional<ButtonType> result = alert.showAndWait();

	        if (result.isEmpty() || result.get() != ButtonType.OK) {
	            mostraMensagem("Jogo Mantido.");
	        	return;
	        }
			
		}
		jDao.excluirEmUso(j);
		jDao.excluir(j);
		mostraMensagem("Jogo excluído.");
        atualizarComboIds();
		limparCampos();
    }
	
	private void alterarJogo() throws ClassNotFoundException, SQLException {
		GenericDao gDao = new GenericDao();
        JogoDao jDao = new JogoDao(gDao);
        Jogo j = new Jogo();
        j.setId(cBIdJogo.getValue());
        j.setNome(tFNomeJogo.getText().trim());
        j.setPreco(Double.parseDouble(tFPrecoJogo.getText().trim()));
        j.setDataLancamento(tFDataJogo.getValue());
        j.setPublicadora(cBPublicadoraJogo.getValue());
        j.setDesenvolvedora(cBDesenvolvedoraJogo.getValue());
        List<Genero> generos = new ArrayList<>();
        for (var entry : estados.entrySet()) {
            if (entry.getValue().get()) {
                generos.add(entry.getKey());
            }
        }
        j.setGeneros(generos);
        jDao.alterar(j);
        mostraMensagem("Jogo alterado.");
        atualizarComboIds();
		limparCampos();
	}
	
	private void listarJogo() throws ClassNotFoundException, SQLException {
        GenericDao gDao = new GenericDao();
        JogoDao jDao = new JogoDao(gDao);
        List<Jogo> jogos = jDao.listar();
        limparCampos();
        StringBuffer buffer = new StringBuffer();
        for(Jogo jo : jogos) {
        	buffer.append(jo.getId() + " - ");
        	buffer.append(jo.getNome() + " - ");
        	buffer.append(jo.getPreco() + " - ");
        	buffer.append(jo.getDataLancamento() + " - ");
        	buffer.append(jo.getPublicadora().getNome() + " - ");
        	buffer.append(jo.getDesenvolvedora().getNome() + " - ");
        	for(Genero ge : jo.getGeneros()) {
        		buffer.append(ge.getNome() + " ");
        	}
        	buffer.append("\n");
        }
		tAJogo.setText(buffer.toString());
    }
	
	private void pesquisarJogo() throws ClassNotFoundException, SQLException {
        GenericDao gDao = new GenericDao();
        JogoDao jDao = new JogoDao(gDao);
        Jogo j = new Jogo();
        j.setId(cBIdJogo.getValue());
        j = jDao.pesquisar(j);
        tFIdJogo.setText(String.valueOf(j.getId()));
        tFNomeJogo.setText(j.getNome());
        tFPrecoJogo.setText(String.valueOf(j.getPreco()));
        tFDataJogo.setValue(j.getDataLancamento());
        cBPublicadoraJogo.setValue(j.getPublicadora());
        cBDesenvolvedoraJogo.setValue(j.getDesenvolvedora());
        for (var entry : estados.entrySet()) {
            entry.getValue().set(j.getGeneros().contains(entry.getKey()));
        }
    }
	
	public void filtrarJogo() throws ClassNotFoundException, SQLException {
		GenericDao gDao = new GenericDao();
	    JogoDao jDao = new JogoDao(gDao);
		List<Genero> generosSel= new ArrayList<>();
	    for (var entry : estados.entrySet()) {
	        if (entry.getValue().get()) { 
	        	generosSel.add(entry.getKey());
	        }
	    }
	    List<Jogo> jogos = jDao.filtrarPorGeneros(generosSel);
	    StringBuffer buffer = new StringBuffer();
	    for(Jogo jo : jogos) {
        	buffer.append(jo.getId() + " - ");
        	buffer.append(jo.getNome() + " - ");
        	buffer.append(jo.getPreco() + " - ");
        	buffer.append(jo.getDataLancamento() + " - ");
        	buffer.append(jo.getPublicadora().getNome() + " - ");
        	buffer.append(jo.getDesenvolvedora().getNome() + " - ");
        	for(Genero ge : jo.getGeneros()) {
        		buffer.append(ge.getNome() + " ");
        	}
        	buffer.append("\n");
        }
		tAJogo.setText(buffer.toString());
	}
	
	public void atualizarComboIds() throws ClassNotFoundException, SQLException {
		GenericDao gDao = new GenericDao();
		JogoDao jDao = new JogoDao(gDao);
        List<Jogo> jogos = jDao.listar();
        cBIdJogo.getItems().clear(); 
        for (Jogo jo : jogos) {
        	cBIdJogo.getItems().add(jo.getId());
        }
	}
	
	private void limparCampos() {
	    tFIdJogo.clear();
	    tFNomeJogo.clear();
	    tFPrecoJogo.clear();
	    tFDataJogo.setValue(LocalDate.now());
	    tAJogo.clear();
	    cBPublicadoraJogo.setValue(null);
	    cBDesenvolvedoraJogo.setValue(null);
	    cBIdJogo.setValue(null);
	    estados.values().forEach(e -> e.set(false));
	}
	
	private void mostraMensagem(String txt) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Aviso");
		alert.setHeaderText(null);
		alert.setContentText(txt);
		alert.showAndWait();
	}
}
    
    
    
    
