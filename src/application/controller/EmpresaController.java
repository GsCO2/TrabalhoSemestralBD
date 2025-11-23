package application.controller;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import application.model.Empresa;
import application.persistence.EmpresaDao;
import application.persistence.GenericDao;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class EmpresaController implements EventHandler<Event> {
	
	private TextField tFIdEmpresa;
	private TextField tFNomeEmpresa;
	private TextField tFPaisEmpresa;
	private TextArea tAEmpresa;
	private ComboBox<Integer> cBIdEmpresa = new ComboBox<>();
	private ComboBox<String> cBTipoEmpresa = new ComboBox<>();

	public EmpresaController(TextField tFIdEmpresa, TextField tFNomeEmpresa, TextField tFPaisEmpresa,
			TextArea tAEmpresa, ComboBox<Integer> cBIdEmpresa, ComboBox<String> cBTipoEmpresa) {
		this.tFIdEmpresa = tFIdEmpresa;
		this.tFNomeEmpresa = tFNomeEmpresa;
		this.tFPaisEmpresa = tFPaisEmpresa;
		this.tAEmpresa = tAEmpresa;
		this.cBIdEmpresa = cBIdEmpresa;
		this.cBTipoEmpresa = cBTipoEmpresa;
	}

	@Override
	public void handle(Event ev) {
		String inp = ev.getSource().toString();
		
		if (inp.contains("Inserir") || inp.contains("Alterar")) {

		    if (tFNomeEmpresa.getText().isBlank() ||
		        tFNomeEmpresa.getText().length() < 2) {
		        mostraMensagem("Nome da empresa inválido.");
		        return;
		    }
		    
		    if(tFNomeEmpresa.getText().length() > 80) {
		    	mostraMensagem("Nome Excedeu o limite de caracteres(80)");
		    	return;
		    }

		    if (tFPaisEmpresa.getText().isBlank() ||
		        tFPaisEmpresa.getText().length() < 2) {
		        mostraMensagem("País inválido.");
		        return;
		    }
		    
		    if(tFPaisEmpresa.getText().length() > 50) {
		    	mostraMensagem("Pais Excedeu o limite de caracteres(50)");
		    	return;
		    }

		    if (cBTipoEmpresa.getValue() == null) {
		        mostraMensagem("Selecione um tipo de empresa.");
		        return;
		    }
		}
		
		if(!inp.contains("Inserir") && !inp.contains("Listar") && !inp.contains("Filtrar")) {
			if(cBIdEmpresa.getValue() == null) {
				mostraMensagem("ID não selecionado, selecione um ID.");
				return;
			}
		}
		
		if(inp.contains("Filtrar")) {
			if (cBTipoEmpresa.getValue() == null) {
		        mostraMensagem("Selecione um tipo de empresa.");
		        return;
		    }
		}
		
		try {
			if(inp.contains("Inserir")) {
				inserirEmpresa();
			} else if(inp.contains("Excluir")) {
				excluirEmpresa();
			} else if(inp.contains("Alterar")) {
				alterarEmpresa();
			} else if(inp.contains("Listar")) {
				listarEmpresa();
			} else if(inp.contains("Pesquisar")) {
				pesquisarEmpresa();
			} else if(inp.contains("Filtrar")) {
				filtrarEmpresa();
			}
		} catch (SQLException | ClassNotFoundException e) {
			e.getMessage();
		}
		
	}

	private void inserirEmpresa() throws ClassNotFoundException, SQLException {
		GenericDao gDao = new GenericDao();
		EmpresaDao eDao = new EmpresaDao(gDao);
		Empresa e = new Empresa();
		e.setNome(tFNomeEmpresa.getText().trim());
		e.setPais(tFPaisEmpresa.getText().trim());
		e.setTipo(cBTipoEmpresa.getValue().trim());
		eDao.inserir(e);
		mostraMensagem("Empresa inserida.");
		atualizarComboIds();
		limparCampos();
	}

	private void excluirEmpresa() throws ClassNotFoundException, SQLException {
		GenericDao gDao = new GenericDao();
		EmpresaDao eDao = new EmpresaDao(gDao);
		Empresa e = new Empresa();
		e.setId(cBIdEmpresa.getValue());
		if(eDao.estaEmUso(e) == true) {
			Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
			alert.setTitle("Empresa possui jogos");
			alert.setHeaderText(null);
			alert.setContentText("Esta empresa possui jogos cadastrados. Todos os jogos e bibliotecas que contem os jogos serão excluídos.\n"
					+ "Deseja excluir mesmo assim?");
			Optional<ButtonType> result = alert.showAndWait();

	        if (result.isEmpty() || result.get() != ButtonType.OK) {
	            mostraMensagem("Empresa Mantida.");
	        	return;
	        }
			
		}
		eDao.excluirEmUso(e);
		eDao.excluir(e);
		mostraMensagem("Empresa excluída.");
        atualizarComboIds();
		limparCampos();
	}

	private void alterarEmpresa() throws ClassNotFoundException, SQLException {
		GenericDao gDao = new GenericDao();
		EmpresaDao eDao = new EmpresaDao(gDao);
		Empresa e = new Empresa();
		e.setId(cBIdEmpresa.getValue());
		e.setNome(tFNomeEmpresa.getText().trim());
		e.setPais(tFPaisEmpresa.getText().trim());
		e.setTipo(cBTipoEmpresa.getValue().trim());
		eDao.alterar(e);
		mostraMensagem("Empresa Alterada.");
		atualizarComboIds();
	    limparCampos(); 
	}

	private void listarEmpresa() throws ClassNotFoundException, SQLException {
		GenericDao gDao = new GenericDao();
		EmpresaDao eDao = new EmpresaDao(gDao);
		List<Empresa> empresas = eDao.listar();
		limparCampos();
		StringBuffer buffer = new StringBuffer();
		for(Empresa em : empresas) {
			buffer.append(em.getId() + " - ");
			buffer.append(em.getNome() + " - ");
			buffer.append(em.getPais() + " - ");
			buffer.append(em.getTipo());
			buffer.append("\n");
		}
		tAEmpresa.setText(buffer.toString());
	}

	private void pesquisarEmpresa() throws ClassNotFoundException, SQLException {
		GenericDao gDao = new GenericDao();
		EmpresaDao eDao = new EmpresaDao(gDao);
		Empresa e = new Empresa();
		e.setId(cBIdEmpresa.getValue());
		e = eDao.pesquisar(e);
		tFIdEmpresa.setText(String.valueOf(e.getId()));
		tFNomeEmpresa.setText(e.getNome());
		tFPaisEmpresa.setText(e.getPais());
		cBTipoEmpresa.setValue(e.getTipo());
	}
	
	private void filtrarEmpresa() throws ClassNotFoundException, SQLException {
		GenericDao gDao = new GenericDao();
		EmpresaDao eDao = new EmpresaDao(gDao);
		List<Empresa> empresas = eDao.filtrar(cBTipoEmpresa.getValue());
		limparCampos();
		StringBuffer buffer = new StringBuffer();
		for(Empresa em : empresas) {
			buffer.append(em.getId() + " - ");
			buffer.append(em.getNome() + " - ");
			buffer.append(em.getPais() + " - ");
			buffer.append(em.getTipo());
			buffer.append("\n");
		}
		tAEmpresa.setText(buffer.toString());
	}
	
	public void atualizarComboIds() throws ClassNotFoundException, SQLException {
		GenericDao gDao = new GenericDao();
		EmpresaDao eDao = new EmpresaDao(gDao);
        List<Empresa> empresas = eDao.listar();
        cBIdEmpresa.getItems().clear(); 
        for (Empresa em : empresas) {
        	cBIdEmpresa.getItems().add(em.getId());
        }
    }

	private void limparCampos() {
		cBIdEmpresa.setValue(null);
		tFIdEmpresa.setText("");
		tFNomeEmpresa.setText("");
		tFPaisEmpresa.setText("");
		cBTipoEmpresa.setValue(null);
	}
	
	private void mostraMensagem(String txt) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Aviso");
		alert.setHeaderText(null);
		alert.setContentText(txt);
		alert.showAndWait();
	}
	
}