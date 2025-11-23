package application.controller.tab;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import application.controller.UsuarioController;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
public class UsuarioTabController {
	
	private TextArea tAUsuario = new TextArea();
	private ComboBox<Integer> cBIdUsuario = new ComboBox<>();
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void adicionarCampos (Tab tab) {
		
		Label labelIdUsuario = new Label ("ID(Ação): ");
		Label labelNomeUsuario = new Label("Nome: ");
		Label labelUsernameUsuario = new Label("Username: ");
		Label labelEmailUsuario = new Label("E-mail: ");
		Label labelSenhaUsuario = new Label("Senha: "); 
		Label labelDataCadastroUsuario = new Label("Data Cadastro: ");
		Label labelCelularUsuario = new Label("Telefone: ");
		Label labelIdPesqUsuario= new Label ("ID(Visualização): ");
		
		labelIdUsuario.setPrefWidth(100);
		labelNomeUsuario.setPrefWidth(100);
		labelUsernameUsuario.setPrefWidth(100);
		labelEmailUsuario.setPrefWidth(100);
		labelSenhaUsuario.setPrefWidth(100);
		labelDataCadastroUsuario.setPrefWidth(100);
		labelCelularUsuario.setPrefWidth(100);
		labelIdPesqUsuario.setPrefWidth(100);
		
		TextField tFNomeUsuario = new TextField();
		TextField tFUsernameUsuario = new TextField();
		TextField tFEmailUsuario = new TextField();
		TextField tFSenhaUsuario = new TextField();
		DatePicker tFDataUsuario = new DatePicker();
		TextField tFCelularUsuario = new TextField();
		TextField tFIdPesqUsuario = new TextField();
		tFIdPesqUsuario.setEditable(false);
		tFDataUsuario.setEditable(false);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

		tFDataUsuario.setConverter(
		    new javafx.util.converter.LocalDateStringConverter(formatter, formatter)
		);
		
		tFDataUsuario.setValue(LocalDate.now());
		tFNomeUsuario.setPrefWidth(230);
		tFNomeUsuario.setMaxWidth(230);
		tFUsernameUsuario.setPrefWidth(130);
		tFUsernameUsuario.setMaxWidth(130);
		tFEmailUsuario.setPrefWidth(230);
		tFEmailUsuario.setMaxWidth(230);
		tFSenhaUsuario.setPrefWidth(150);
		tFSenhaUsuario.setMaxWidth(150);
		tFDataUsuario.setPrefWidth(105);
		tFDataUsuario.setMaxWidth(105);
		tFCelularUsuario.setPrefWidth(110);
		tFCelularUsuario.setMaxWidth(110);
		tFIdPesqUsuario.setPrefWidth(95);
		tFIdPesqUsuario.setPrefWidth(95);
		tAUsuario.setEditable(false);
		tAUsuario.setPrefWidth(390);
		tAUsuario.setMaxWidth(390);
		tAUsuario.setPrefHeight(195);
		tAUsuario.setMaxHeight(195);	
		
		
		Button btnPesquisar = new Button("Pesquisar");
		Button btnInserir = new Button("Inserir");
		Button btnExcluir = new Button("Excluir");
		Button btnAlterar = new Button("Alterar");
		Button btnListar = new Button("Listar");
		Button btnTotal = new Button("Total");
		
		btnPesquisar.setPrefWidth(130);
		btnInserir.setPrefWidth(95);
		btnExcluir.setPrefWidth(95);
		btnAlterar.setPrefWidth(95);
		btnListar.setPrefWidth(95);
		btnTotal.setPrefWidth(95);
		
		tFCelularUsuario.textProperty().addListener((observable, oldValue, newValue) -> {
		    if (!newValue.matches("\\d*")) {
		        tFCelularUsuario.setText(oldValue);
		    }
		});
		
		EventHandler ev = new UsuarioController(tFIdPesqUsuario, tFNomeUsuario, tFUsernameUsuario,
				tFEmailUsuario, tFSenhaUsuario, tFDataUsuario, tFCelularUsuario,
				tAUsuario, cBIdUsuario);
		try {
			((UsuarioController) ev).atualizarComboIds();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		btnPesquisar.setOnAction(ev);
		btnInserir.setOnAction(ev);
		btnExcluir.setOnAction(ev);
		btnAlterar.setOnAction(ev);
		btnListar.setOnAction(ev);
		btnTotal.setOnAction(ev);
		
		HBox priLinha = new HBox(4);
		HBox segLinha = new HBox(2);
		HBox terLinha = new HBox(2);
		HBox quaLinha = new HBox(2);
		HBox quiLinha = new HBox(2);
		HBox sexLinha = new HBox(2);
		HBox setLinha = new HBox(2);
		HBox oitLinha = new HBox(2);
		HBox nonLinha = new HBox(4);
		VBox vBox = new VBox();
		priLinha.getChildren().addAll(labelIdUsuario, cBIdUsuario, btnPesquisar, btnTotal);
		segLinha.getChildren().addAll(labelIdPesqUsuario, tFIdPesqUsuario);
		terLinha.getChildren().addAll(labelNomeUsuario, tFNomeUsuario);
		quaLinha.getChildren().addAll(labelUsernameUsuario, tFUsernameUsuario);
		quiLinha.getChildren().addAll(labelEmailUsuario, tFEmailUsuario);
		sexLinha.getChildren().addAll(labelSenhaUsuario, tFSenhaUsuario);
		setLinha.getChildren().addAll(labelDataCadastroUsuario, tFDataUsuario);
		oitLinha.getChildren().addAll(labelCelularUsuario, tFCelularUsuario);
		nonLinha.getChildren().addAll(btnInserir, btnExcluir, btnAlterar, btnListar);
		Insets insets = new Insets(25, 12, 12, 125);
		vBox.setPadding(insets);
		vBox.getChildren().addAll(priLinha, segLinha, terLinha, quaLinha, quiLinha, 
				sexLinha,setLinha, oitLinha, nonLinha, tAUsuario);
		tab.setContent(vBox);
	}
	
	public void reiniciaTabUsuario(Tab tab) throws ClassNotFoundException, SQLException {
		tab.setContent(null);
		adicionarCampos(tab);
	}
	
}
