package application.controller.tab;

import java.sql.SQLException;

import application.controller.GeneroController;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class GeneroTabController {
	
	private TextArea tAGenero = new TextArea();
	private ComboBox<Integer> cBIdGenero= new ComboBox<>();
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void adicionarCampos (Tab tab) {
		
		Label labelIdGenero = new Label ("ID(Ação): ");
		Label labelIdPesqGenero = new Label ("ID(Visualização): ");
		Label labelNomeGenero = new Label("Nome: ");
		
		labelIdGenero.setPrefWidth(100);
		labelIdPesqGenero.setPrefWidth(100);
		labelNomeGenero.setPrefWidth(100);
		
		TextField tFIdGenero = new TextField();
		tFIdGenero.setEditable(false);
		TextField tFNomeGenero = new TextField();
		
		tFIdGenero.setPrefWidth(95);
		tFIdGenero.setMaxWidth(95);
		tFNomeGenero.setPrefWidth(230);
		tFNomeGenero.setMaxWidth(230);
		tAGenero.setEditable(false);
		tAGenero.setPrefWidth(390);
		tAGenero.setMaxWidth(390);
		tAGenero.setPrefHeight(195);
		tAGenero.setMaxHeight(195);	
		
		Button btnPesquisar = new Button("Pesquisar");
		Button btnInserir = new Button("Inserir");
		Button btnExcluir = new Button("Excluir");
		Button btnAlterar = new Button("Alterar");
		Button btnListar = new Button("Listar");
		btnPesquisar.setPrefWidth(130);
		btnInserir.setPrefWidth(95);
		btnExcluir.setPrefWidth(95);
		btnAlterar.setPrefWidth(95);
		btnListar.setPrefWidth(95);
		
		EventHandler ev = new GeneroController(tFIdGenero, tFNomeGenero, tAGenero, cBIdGenero);
		try {
			((GeneroController) ev).atualizarComboIds();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		btnPesquisar.setOnAction(ev);
		btnInserir.setOnAction(ev);
		btnExcluir.setOnAction(ev);
		btnAlterar.setOnAction(ev);
		btnListar.setOnAction(ev);
		
		HBox pLinha = new HBox(3);
		HBox sLinha = new HBox(2);
		HBox tLinha = new HBox(2);
		HBox qLinha = new HBox(4);
		VBox vBox = new VBox();
		pLinha.getChildren().addAll(labelIdGenero, cBIdGenero, btnPesquisar);
		sLinha.getChildren().addAll(labelIdPesqGenero, tFIdGenero);
		tLinha.getChildren().addAll(labelNomeGenero, tFNomeGenero);
		qLinha.getChildren().addAll(btnInserir, btnExcluir, btnAlterar, btnListar);
		Insets insets = new Insets(25, 12, 12, 125);
		vBox.setPadding(insets);
		vBox.getChildren().addAll(pLinha, sLinha, tLinha, qLinha, tAGenero);
		tab.setContent(vBox);
	}
	
	public void reiniciaTabGenero(Tab tab) throws ClassNotFoundException, SQLException {
		tab.setContent(null);
		
		adicionarCampos(tab);
	}
}
