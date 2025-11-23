package application.controller;


import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import application.model.Genero;
import application.persistence.GenericDao;
import application.persistence.GeneroDao;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class GeneroController implements EventHandler<Event> {
	
	private TextField tFIdGenero;
	private TextField tFNomeGenero;
	private TextArea tAGenero;
	private ComboBox<Integer> cBIdGenero = new ComboBox<>();

	public GeneroController(TextField tFIdGenero, TextField tFNomeGenero, 
		TextArea taListaGenero, ComboBox<Integer> cBIdGenero) {
		this.tFIdGenero = tFIdGenero;
		this.tFNomeGenero = tFNomeGenero;
		this.tAGenero = taListaGenero;
		this.cBIdGenero = cBIdGenero;
	}

	@Override
	public void handle(Event ev) {
		String inp = ev.getSource().toString();

		if(inp.contains("Inserir") || inp.contains("Alterar")) {
			if(tFNomeGenero.getText().isBlank() || 
					tFNomeGenero.getText().length() < 2 ) {
				mostraMensagem("Nome Inválido, Digite Novamente.");
				return;
			}
			
			if(tFNomeGenero.getText().length() > 30) {
				mostraMensagem("Nome Excedeu o limite de caracteres(30)");
			}
		}
		
		if(!inp.contains("Inserir") && !inp.contains("Listar")) {
			if(cBIdGenero.getValue() == null) {
				mostraMensagem("ID não selecionado, selecione um ID.");
				return;
			}
		}
		
		if(inp.contains("Inserir")) {
			try {
				if(verificarUnique("nome", tFNomeGenero.getText()) == true) {
					mostraMensagem("Genero já cadastrado.");
					return;
				}
				
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}	
		}
		
		if(inp.contains("Alterar")) {
			try {
				if(!verSeExiste()){
					return;
				}
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		}
		
		try {
			if(inp.contains("Inserir")) {
				inserirGenero();
			} else if(inp.contains("Excluir")) {
				excluirGenero();
			} else if(inp.contains("Alterar")) {
				alterarGenero();
			} else if(inp.contains("Listar")) {
				listarGenero();
			} else if(inp.contains("Pesquisar")) {
				pesquisarGenero();
			}
		} catch (SQLException | ClassNotFoundException e) {
			e.getMessage();
		}
		
	}

	private void inserirGenero() throws ClassNotFoundException, SQLException {
		GenericDao gDao = new GenericDao();
		GeneroDao geDao = new GeneroDao(gDao);
		Genero g = new Genero();
		g.setNome(tFNomeGenero.getText().trim());
		geDao.inserir(g);
		mostraMensagem("Gênero inserido.");
		atualizarComboIds();
		limparCampos();
		
	}

	private void excluirGenero() throws ClassNotFoundException, SQLException {
		GenericDao gDao = new GenericDao();
		GeneroDao geDao = new GeneroDao(gDao);
		Genero g = new Genero();
		g.setId(cBIdGenero.getValue());
		if(geDao.estaEmUso(g) == true) {
			Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
			alert.setTitle("Gênero em uso");
			alert.setHeaderText(null);
			alert.setContentText("Este Gênero está sendo utilizado, Deseja excluir mesmo assim?");
			Optional<ButtonType> result = alert.showAndWait();

	        if (result.isEmpty() || result.get() != ButtonType.OK) {
	            mostraMensagem("Genero Mantido.");
	        	return;
	        }
			
		}
		geDao.excluirEmUso(g);
		geDao.excluir(g);
		mostraMensagem("Gênero excluído.");
        atualizarComboIds();
		limparCampos();
	}

	private void alterarGenero() throws ClassNotFoundException, SQLException {
		GenericDao gDao = new GenericDao();
		GeneroDao geDao = new GeneroDao(gDao);
		Genero g = new Genero();
        g.setId(cBIdGenero.getValue());
        g.setNome(tFNomeGenero.getText().trim());
		geDao.alterar(g);
		mostraMensagem("Gênero Alterado.");
		atualizarComboIds();
	    limparCampos(); 
	}

	private void listarGenero() throws ClassNotFoundException, SQLException {
		GenericDao gDao = new GenericDao();
		GeneroDao geDao = new GeneroDao(gDao);
		List<Genero> generos = geDao.listar();
		limparCampos();
		StringBuffer buffer = new StringBuffer();
		for(Genero ge : generos) {
			buffer.append(ge.getId() + " - ");
			buffer.append(ge.getNome());
			buffer.append("\n");
		}
		tAGenero.setText(buffer.toString());
	}

	private void pesquisarGenero() throws ClassNotFoundException, SQLException {
		GenericDao gDao = new GenericDao();
		GeneroDao geDao = new GeneroDao(gDao);
		Genero g = new Genero();
		g.setId(cBIdGenero.getValue());
		g = geDao.pesquisar(g);
		tFIdGenero.setText(String.valueOf(g.getId()));
		tFNomeGenero.setText(g.getNome());
	}
	
	private boolean verificarUnique(String string, String text, int id) throws ClassNotFoundException, SQLException {
		GenericDao gDao = new GenericDao();
		GeneroDao geDao = new GeneroDao(gDao);
		return geDao.verificarUnique(string, text, id);
	}
	
	private boolean verificarUnique(String campo, String unique) throws ClassNotFoundException, SQLException {
		GenericDao gDao = new GenericDao();
        GeneroDao uDao = new GeneroDao(gDao);
		return uDao.verificarUnique(campo, unique, 0);
	}
	
	private boolean verSeExiste() throws ClassNotFoundException, SQLException {
		GenericDao gDao = new GenericDao();
		GeneroDao geDao = new GeneroDao(gDao);
		Genero gAtual = new Genero();
		gAtual.setId(cBIdGenero.getValue());
		gAtual = geDao.pesquisar(gAtual);
		if(!tFNomeGenero.getText().equals(gAtual.getNome())) {
	        if(verificarUnique ("nome", tFNomeGenero.getText(), gAtual.getId()) == true) {
	            mostraMensagem("Nome já cadastrado.");
	            return false;
	        }
	    }
		return true;
	}

	public void atualizarComboIds() throws ClassNotFoundException, SQLException {
        GenericDao gDao = new GenericDao();
        GeneroDao geDao = new GeneroDao(gDao);
        List<Genero> generos = geDao.listar();
        cBIdGenero.getItems().clear();
        for (Genero ge : generos) {
            cBIdGenero.getItems().add(ge.getId());
        }
    }

	private void limparCampos() {
		cBIdGenero.setValue(null);
		tFIdGenero.setText("");
		tFNomeGenero.setText("");
	}
	
	private void mostraMensagem(String txt) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Aviso");
		alert.setHeaderText(null);
		alert.setContentText(txt);
		alert.showAndWait();
	}
	
}
	
