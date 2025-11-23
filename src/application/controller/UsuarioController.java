package application.controller;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import application.model.Usuario;
import application.persistence.GenericDao;
import application.persistence.UsuarioDao;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class UsuarioController implements EventHandler<Event> {
	
	private TextField tFIdUsuario;
	private TextField tFNomeUsuario;
	private TextField tFUsernameUsuario;
	private TextField tFEmailUsuario;
	private TextField tFSenhaUsuario;
	private DatePicker tFDataUsuario;
	private TextField tFCelularUsuario;
	private TextArea tAUsuario;
	private ComboBox<Integer> cBIdUsuario= new ComboBox<>();
	
	public UsuarioController(TextField tFIdUsuario, TextField tFNomeUsuario, TextField tFUsernameUsuario,
			TextField tFEmailUsuario, TextField tFSenhaUsuario, DatePicker tFDataUsuario, TextField tFCelularUsuario,
			TextArea tAUsuario, ComboBox<Integer> cBIdUsuario) {
		this.tFIdUsuario = tFIdUsuario;
		this.tFNomeUsuario = tFNomeUsuario;
		this.tFUsernameUsuario = tFUsernameUsuario;
		this.tFEmailUsuario = tFEmailUsuario;
		this.tFSenhaUsuario = tFSenhaUsuario;
		this.tFDataUsuario = tFDataUsuario;
		this.tFCelularUsuario = tFCelularUsuario;
		this.tAUsuario = tAUsuario;
		this.cBIdUsuario = cBIdUsuario;
	}
	
	@Override
	public void handle(Event ev) {
		String inp = ev.getSource().toString();
		
		if(inp.contains("Inserir") || inp.contains("Alterar")) {
			if(tFNomeUsuario.getText().isBlank() || 
					tFNomeUsuario.getText().length() < 2 ) {
				mostraMensagem("Nome Inválido, Digite Novamente.");
				return;
			}
			
			if(tFUsernameUsuario.getText().isBlank() || 
					tFUsernameUsuario.getText().length() < 2 ) {
				mostraMensagem("Username Inválido, Digite Novamente.");
				return;
			}
			
			if(tFSenhaUsuario.getText().isBlank() || 
					tFSenhaUsuario.getText().length() < 6 ) {
				mostraMensagem("Senha Inválido, Digite Novamente.");
				return;
			}
			
			if(validarEmail() == false) {
				mostraMensagem("Email Inválido, Digite Novamente.");
				return;
			}
			
			if(tFSenhaUsuario.getText().isBlank() || 
					tFSenhaUsuario.getText().length() < 6 ) {
				mostraMensagem("Senha Inválida, Digite Novamente.");
				return;
			}
			
			if(tFCelularUsuario.getText().isBlank() || 
					tFCelularUsuario.getText().length() < 12 || tFCelularUsuario.getText().length() > 14) {
				mostraMensagem("Celular Inválido, Digite Novamente.");
				return;
			}
				
			
			if(tFNomeUsuario.getText().length() > 80) {
				mostraMensagem("Nome Excedeu o limite de caracteres(80)");
				return;
			}
			
			if(tFUsernameUsuario.getText().length() > 16) {
				mostraMensagem("Username Excedeu o limite de caracteres(16)");
				return;
			}
			
			if(tFEmailUsuario.getText().length() > 60) {
				mostraMensagem("Email Excedeu o limite de caracteres(60)");
				return;
			}
			
			if(tFSenhaUsuario.getText().length() > 12) {
				mostraMensagem("Senha Excedeu o limite de caracteres(12)");
				return;
			}
			
			if(tFDataUsuario.getValue().isAfter(LocalDate.now())) {
				mostraMensagem("Data de Cadastro Inválida");
				return;
			}
			
			if(tFCelularUsuario.getText().length() > 15) {
				mostraMensagem("Celular Excedeu o limite de caracteres(15)");
				return;
			}
		
		}
		
		if(inp.contains("Inserir")) {
			try {
				if(verificarUnique("username", tFUsernameUsuario.getText()) == true) {
					mostraMensagem("Username já cadastrado.");
					return;
				}
				
				if(verificarUnique("email", tFEmailUsuario.getText()) == true) {
					mostraMensagem("Email já cadastrado.");
					return;
				}
				
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}	
		}
		
		if(inp.contains("Alterar")) {
			try {
				if(!verSeExiste()) {
					return;
				}
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		}

		if(!inp.contains("Inserir") && !inp.contains("Listar") && !inp.contains("Total")) {
			if(cBIdUsuario.getValue() == null) {
				mostraMensagem("ID não selecionado, selecione um ID.");
				return;
			}
		}
		
		try {
			if(inp.contains("Inserir")) {
				inserirUsuario();
			} else if(inp.contains("Excluir")) {
				excluirUsuario();
			} else if(inp.contains("Alterar")) {
				alterarUsuario();
			} else if(inp.contains("Listar")) {
				listarUsuario();
			} else if(inp.contains("Pesquisar")) {
				pesquisarUsuario();
			} else if(inp.contains("Total")) {
				totalizarUsuario();
			}
		} catch (SQLException | ClassNotFoundException e) {
			e.getMessage();
		}
		
	}

	private void inserirUsuario() throws ClassNotFoundException, SQLException {
		GenericDao gDao = new GenericDao();
		UsuarioDao uDao = new UsuarioDao(gDao);
		Usuario u = new Usuario();
		u.setNome(tFNomeUsuario.getText().trim());
		u.setUsername(tFUsernameUsuario.getText().trim());
		u.setEmail(tFEmailUsuario.getText().trim());
		u.setSenha(tFSenhaUsuario.getText().trim());
		u.setData(tFDataUsuario.getValue());
		u.setNumCelular(tFCelularUsuario.getText().trim());
		uDao.inserir(u);
		mostraMensagem("Conta de usuário inserida.");
		atualizarComboIds();
		limparCampos();
	}

	private void excluirUsuario() throws ClassNotFoundException, SQLException {
		GenericDao gDao = new GenericDao();
		UsuarioDao uDao = new UsuarioDao(gDao);
		Usuario u = new Usuario();
		u.setId(cBIdUsuario.getValue());
		if(uDao.estaEmUso(u) == true) {
			Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
			alert.setTitle("Usuário possui biblioteca");
			alert.setHeaderText(null);
			alert.setContentText("Este usuário possui jogos na biblioteca. A biblioteca e seus jogos serão excluídos.\n"
					+ "eseja excluir mesmo assim?");
			Optional<ButtonType> result = alert.showAndWait();

	        if (result.isEmpty() || result.get() != ButtonType.OK) {
	            mostraMensagem("Usuario Mantido.");
	        	return;
	        }
			
		}
		uDao.excluirEmUso(u);
		uDao.excluir(u);
		mostraMensagem("Usuario excluído.");
        atualizarComboIds();
		limparCampos();
	}

	private void alterarUsuario() throws ClassNotFoundException, SQLException {
		GenericDao gDao = new GenericDao();
		UsuarioDao uDao = new UsuarioDao(gDao);
		Usuario u = new Usuario();
		u.setId(cBIdUsuario.getValue());
		u.setNome(tFNomeUsuario.getText().trim());
		u.setUsername(tFUsernameUsuario.getText().trim());
		u.setEmail(tFEmailUsuario.getText().trim());
		u.setSenha(tFSenhaUsuario.getText().trim());
		u.setData(tFDataUsuario.getValue());
		u.setNumCelular(tFCelularUsuario.getText().trim());
		uDao.alterar(u);
		mostraMensagem("Conta de usuário alterada.");
		atualizarComboIds();
	    limparCampos(); 
	}

	private void listarUsuario() throws ClassNotFoundException, SQLException {
		GenericDao gDao = new GenericDao();
		UsuarioDao uDao = new UsuarioDao(gDao);
		List<Usuario> usuarios = uDao.listar();
		limparCampos();
		StringBuffer buffer = new StringBuffer();
		for(Usuario us : usuarios) {
			buffer.append(us.getId() + " - ");
			buffer.append(us.getNome() + " - ");
			buffer.append(us.getUsername() + " - ");
			buffer.append(us.getEmail() + " - ");
			buffer.append(us.getSenha() + " - ");
			buffer.append(us.getData().toString() + " - ");
			buffer.append(us.getNumCelular());
			buffer.append("\n");
		}
		tAUsuario.setText(buffer.toString());
	}

	private void pesquisarUsuario() throws ClassNotFoundException, SQLException {
		GenericDao gDao = new GenericDao();
		UsuarioDao uDao = new UsuarioDao(gDao);
		Usuario u = new Usuario();
		u.setId(cBIdUsuario.getValue());
		u = uDao.pesquisar(u);
		tFIdUsuario.setText(String.valueOf(u.getId()));
		tFNomeUsuario.setText(u.getNome());
		tFUsernameUsuario.setText(u.getUsername());
		tFEmailUsuario.setText(u.getEmail());
		tFSenhaUsuario.setText(u.getSenha());
		tFDataUsuario.setValue(u.getData());
		tFCelularUsuario.setText(u.getNumCelular());
	}
	
	private void totalizarUsuario() throws ClassNotFoundException, SQLException {
		GenericDao gDao = new GenericDao();
		UsuarioDao uDao = new UsuarioDao(gDao);
		int total = uDao.contarTotal();
		mostraMensagem("Total de Usuários: " + total);
	}
	
	private boolean validarEmail() {
		if(tFEmailUsuario.getText().isBlank() || 
				tFEmailUsuario.getText().length() < 12) {
			return false;
		}
		
		if(!tFEmailUsuario.getText().contains("@") || tFEmailUsuario.getText().startsWith("@")) {
			return false;
		}
		
		String[] emailSplitado = tFEmailUsuario.getText().split("@");
		
		if(emailSplitado.length != 2) {
			return false;
		}
		
		if(!emailSplitado[1].contains(".") || emailSplitado[1].startsWith(".") || emailSplitado[1].endsWith(".")) {
			return false;
		}
		
		return true;
	}
	
	private boolean verificarUnique(String campo, String unique, int id) throws ClassNotFoundException, SQLException {
		GenericDao gDao = new GenericDao();
        UsuarioDao uDao = new UsuarioDao(gDao);
		return uDao.verificarUnique(campo, unique, id);
	}
	
	private boolean verificarUnique(String campo, String unique) throws ClassNotFoundException, SQLException {
		GenericDao gDao = new GenericDao();
        UsuarioDao uDao = new UsuarioDao(gDao);
		return uDao.verificarUnique(campo, unique, 0);
	}
	
	
	private boolean verSeExiste() throws ClassNotFoundException, SQLException {
	    GenericDao gDao = new GenericDao();
	    UsuarioDao uDao = new UsuarioDao(gDao);
	    Usuario uAtual = new Usuario();
	    uAtual.setId(cBIdUsuario.getValue());
	    uAtual = uDao.pesquisar(uAtual);
	    if(!tFUsernameUsuario.getText().equals(uAtual.getUsername())) {
	        if(verificarUnique ("username", tFUsernameUsuario.getText(), uAtual.getId()) == true) {
	            mostraMensagem("Username já cadastrado.");
	            return false;
	        }
	    }
	    if(!tFEmailUsuario.getText().equals(uAtual.getEmail())) {
	        if(verificarUnique("email", tFEmailUsuario.getText(), uAtual.getId()) == true) {
	            mostraMensagem("Email já cadastrado.");
	            return false;
	        }
	    }
	    return true;
	}
	public void atualizarComboIds() throws ClassNotFoundException, SQLException {
        GenericDao gDao = new GenericDao();
        UsuarioDao uDao = new UsuarioDao(gDao);
        List<Usuario> usuarios = uDao.listar();
        cBIdUsuario.getItems().clear();
        for (Usuario us : usuarios) {
            cBIdUsuario.getItems().add(us.getId());
        }
    }

	private void limparCampos() {
		cBIdUsuario.setValue(null);
		tFIdUsuario.setText("");
		tFNomeUsuario.setText("");
		tFUsernameUsuario.setText("");
		tFEmailUsuario.setText("");
		tFSenhaUsuario.setText("");
		tFDataUsuario.setValue(LocalDate.now());
		tFCelularUsuario.setText("");
	}
	
	private void mostraMensagem(String txt) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Aviso");
		alert.setHeaderText(null);
		alert.setContentText(txt);
		alert.showAndWait();
	}
	
}
	
