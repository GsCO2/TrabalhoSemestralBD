package application.controller.tab;


import java.sql.SQLException;

import application.controller.EmpresaController;
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

public class EmpresaTabController {
	
	private TextArea tAEmpresa = new TextArea();
	private ComboBox<Integer> cBIdEmpresa = new ComboBox<>();
	private ComboBox<String> cBTipoEmpresa = new ComboBox<>();
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void adicionarCampos (Tab tab) {
		
		Label labelIdEmpresa = new Label ("ID(Ação): ");
		Label labelNomeEmpresa = new Label("Nome: ");
		Label labelPaisEmpresa = new Label("Pais: ");
		Label labelTipoEmpresa = new Label("Tipo: ");
		Label labelIdPesqEmpresa = new Label ("ID(Visualização): ");
		
		labelIdEmpresa.setPrefWidth(100);
		labelIdPesqEmpresa.setPrefWidth(100);
		labelNomeEmpresa.setPrefWidth(100);
		labelPaisEmpresa.setPrefWidth(100);
		
		cBTipoEmpresa.getItems().clear();
		cBTipoEmpresa.getItems().addAll("Desenvolvedora", "Publicadora", "Ambas");
		
		TextField tFIdEmpresa = new TextField();
		tFIdEmpresa.setEditable(false);
		TextField tFNomeEmpresa = new TextField();
		TextField tFPaisEmpresa = new TextField();
		
		tFIdEmpresa.setPrefWidth(95);
		tFIdEmpresa.setMaxWidth(95);
		tFNomeEmpresa.setPrefWidth(230);
		tFNomeEmpresa.setMaxWidth(230);
		tFPaisEmpresa.setPrefWidth(95);
		tFPaisEmpresa.setMaxWidth(95);
		tAEmpresa.setEditable(false);
		tAEmpresa.setPrefWidth(390);
		tAEmpresa.setMaxWidth(390);
		tAEmpresa.setPrefHeight(195);
		tAEmpresa.setMaxHeight(195);	
		
		Button btnFiltrar = new Button("Filtrar");
		Button btnPesquisar = new Button("Pesquisar");
		Button btnInserir = new Button("Inserir");
		Button btnExcluir = new Button("Excluir");
		Button btnAlterar = new Button("Alterar");
		Button btnListar = new Button("Listar");
		
		btnPesquisar.setPrefWidth(130);
		btnFiltrar.setPrefWidth(95);
		btnInserir.setPrefWidth(95);
		btnExcluir.setPrefWidth(95);
		btnAlterar.setPrefWidth(95);
		btnListar.setPrefWidth(95);
		cBTipoEmpresa.setPrefWidth(140);
		cBTipoEmpresa.setMaxWidth(140);
		
		EventHandler ev = new EmpresaController(tFIdEmpresa, tFNomeEmpresa, tFPaisEmpresa, 
				tAEmpresa, cBIdEmpresa, cBTipoEmpresa);
		try {
			((EmpresaController) ev).atualizarComboIds();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		btnPesquisar.setOnAction(ev);
		btnInserir.setOnAction(ev);
		btnExcluir.setOnAction(ev);
		btnAlterar.setOnAction(ev);
		btnListar.setOnAction(ev);
		btnFiltrar.setOnAction(ev);
		
		HBox priLinha = new HBox(3);
		HBox segLinha = new HBox(2);
		HBox terLinha = new HBox(2);
		HBox quaLinha = new HBox(2);
		HBox quiLinha = new HBox(4);
		HBox sexLinha = new HBox(4);
		VBox vBox = new VBox();
		priLinha.getChildren().addAll(labelIdEmpresa, cBIdEmpresa, btnPesquisar);
		segLinha.getChildren().addAll(labelIdPesqEmpresa, tFIdEmpresa);
		terLinha.getChildren().addAll(labelNomeEmpresa, tFNomeEmpresa);
		quaLinha.getChildren().addAll(labelPaisEmpresa, tFPaisEmpresa);
		quiLinha.getChildren().addAll(labelTipoEmpresa, new Label("                   "),
				cBTipoEmpresa, btnFiltrar);
		sexLinha.getChildren().addAll(btnInserir, btnExcluir, btnAlterar, btnListar);
		Insets insets = new Insets(25, 12, 12, 125);
		vBox.setPadding(insets);
		vBox.getChildren().addAll(priLinha, segLinha, terLinha, quaLinha, quiLinha, sexLinha, tAEmpresa);
		tab.setContent(vBox);
	}
	
	public void reiniciaTabEmpresa(Tab tab) throws ClassNotFoundException, SQLException {
		tab.setContent(null);
		adicionarCampos(tab);
	}
}
