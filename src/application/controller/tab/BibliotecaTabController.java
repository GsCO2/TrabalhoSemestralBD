package application.controller.tab;

import application.model.Jogo;
import application.model.Usuario;
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

import java.sql.SQLException;
import java.time.LocalDate;

import application.controller.BibliotecaController;

public class BibliotecaTabController {
	
	private ComboBox <Usuario> cBUsuarioBib = new ComboBox<Usuario>();
	private ComboBox <Jogo> cBJogoBib = new ComboBox<Jogo>();
	private TextArea tABib = new TextArea();
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void adicionarCampos (Tab tab) {
		
		Label labelUsernameBib = new Label("Username: ");
		Label labelJogoBib = new Label("Jogo: ");
		Label labelDataBib = new Label("Data Compra: ");
		Label labelHorasBib = new Label("Horas Jogadas: ");
		
		labelUsernameBib.setPrefWidth(100);
		labelJogoBib.setPrefWidth(100);
		labelDataBib.setPrefWidth(100);
		labelHorasBib.setPrefWidth(100);
		
		DatePicker tFDataBib = new DatePicker();
		tFDataBib.setEditable(false);
		tFDataBib.setValue(LocalDate.now());
		TextField tFHorasBib = new TextField();
		tFHorasBib.setText("0");
		cBUsuarioBib.setPrefWidth(130);
		cBUsuarioBib.setMaxWidth(130);
		cBJogoBib.setPrefWidth(130);
		cBJogoBib.setMaxWidth(130);
		tFDataBib.setPrefWidth(105);
		tFDataBib.setMaxWidth(105);
		tFHorasBib.setPrefWidth(95);
		tFHorasBib.setMaxWidth(95);
		
		tABib.setPrefWidth(390);
		tABib.setMaxWidth(390);
		tABib.setPrefHeight(195);
		tABib.setMaxHeight(195);	
		tABib.setEditable(false);
		
		tFHorasBib.textProperty().addListener((observable, oldValue, newValue) -> {
		    if (!newValue.matches("\\d*")) {
		    	tFHorasBib.setText(oldValue);
		    }
		});
		
		Button btnPesquisar = new Button("Pesquisar");
		Button btnInserir = new Button("Inserir");
		Button btnExcluir = new Button("Excluir");
		Button btnAlterar = new Button("Alterar");
		Button btnListar = new Button("Listar");
		Button btnFiltrar = new Button("Filtrar");
		Button btnTotal= new Button("Total");
		btnPesquisar.setPrefWidth(130);
		btnInserir.setPrefWidth(95);
		btnExcluir.setPrefWidth(95);
		btnAlterar.setPrefWidth(95);
		btnListar.setPrefWidth(95);
		btnFiltrar.setPrefWidth(95);
		btnTotal.setPrefWidth(95);
		
		EventHandler ev = new BibliotecaController(cBUsuarioBib, cBJogoBib, tABib, tFDataBib, tFHorasBib);
		try {
			((BibliotecaController) ev).atualizarComboIds();
			
		} catch (ClassNotFoundException | SQLException e) {
		    e.printStackTrace();
		}
		btnPesquisar.setOnAction(ev);
		btnInserir.setOnAction(ev);
		btnExcluir.setOnAction(ev);
		btnAlterar.setOnAction(ev);
		btnListar.setOnAction(ev);
		btnFiltrar.setOnAction(ev);
		btnTotal.setOnAction(ev);
		
		HBox priLinha = new HBox(4);
		HBox segLinha = new HBox(3);
		HBox terLinha = new HBox(2);
		HBox quaLinha = new HBox(2);
		HBox quiLinha = new HBox(3);
		VBox vBox = new VBox();
		priLinha.getChildren().addAll(labelUsernameBib, cBUsuarioBib, btnListar, btnTotal);
		segLinha.getChildren().addAll(labelJogoBib, cBJogoBib, btnPesquisar);
		terLinha.getChildren().addAll(labelDataBib, tFDataBib, btnFiltrar);
		quaLinha.getChildren().addAll(labelHorasBib, tFHorasBib);
		quiLinha.getChildren().addAll(btnInserir, btnExcluir, btnAlterar);
		Insets insets = new Insets(25, 12, 12, 125);
		vBox.setPadding(insets);
		vBox.getChildren().addAll(priLinha, segLinha, terLinha,
				quaLinha, quiLinha, tABib);
		tab.setContent(vBox);
	}
	
	public void reiniciaTabBiblioteca(Tab tab) throws ClassNotFoundException, SQLException {
		tab.setContent(null);
		adicionarCampos(tab);
	}
}
