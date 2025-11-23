package application.controller.tab;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import application.controller.JogoController;
import application.model.Empresa;
import application.model.Genero;
import application.persistence.EmpresaDao;
import application.persistence.GenericDao;
import application.persistence.GeneroDao;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class JogoTabController {

	private TextArea tAJogo = new TextArea();
	private ComboBox<Empresa> cBPublicadoraJogo = new ComboBox<>();
	private ComboBox<Empresa> cBDesenvolvedoraJogo = new ComboBox<>();
	private ComboBox<Integer> cBIdJogo = new ComboBox<>();
	private ListView<Genero> lVGeneroJogo = new ListView<>(); 
	private ObservableMap<Genero, BooleanProperty> estados = FXCollections.observableHashMap();
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void adicionarCampos (Tab tab) {
		lVGeneroJogo.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		Label labelIdJogo = new Label ("ID(Ação): ");
		Label labelIdPesqJogo= new Label ("ID(Visualização): ");
		Label labelNomeJogo = new Label("Nome: ");
		Label labelPrecoJogo = new Label("Preço: ");
		Label labelDataJogo = new Label("Data Lançamento: ");
		Label labelPublicadoraJogo = new Label("Publicadora: ");
		Label labelDesenvolvedoraJogo = new Label("Desenvolvedora: ");
		Label labelGeneroJogo = new Label("Genero: ");
		
		lVGeneroJogo.setCellFactory(CheckBoxListCell.forListView(
	            genero -> estados.computeIfAbsent(genero, g -> new SimpleBooleanProperty(false))
	        ));
		
		labelIdJogo.setPrefWidth(100);
		labelNomeJogo.setPrefWidth(100);
		labelPrecoJogo.setPrefWidth(100);
		labelDataJogo.setPrefWidth(100);
		labelPublicadoraJogo.setPrefWidth(100);
		labelDesenvolvedoraJogo.setPrefWidth(100);
		labelGeneroJogo.setPrefWidth(100);
		
		TextField tFIdJogo = new TextField();
		TextField tFNomeJogo = new TextField();
		TextField tFPrecoJogo = new TextField();
		DatePicker tFDataJogo = new DatePicker();
		tFIdJogo.setEditable(false);
		tFDataJogo.setEditable(false);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

		tFDataJogo.setConverter(
		    new javafx.util.converter.LocalDateStringConverter(formatter, formatter)
		);
		
		tFDataJogo.setValue(LocalDate.now());
		
		tFIdJogo.setPrefWidth(95);
		tFIdJogo.setMaxWidth(95);
		tFNomeJogo.setPrefWidth(230);
		tFNomeJogo.setMaxWidth(230);
		tFDataJogo.setPrefWidth(105);
		tFDataJogo.setMaxWidth(105);
		tFPrecoJogo.setPrefWidth(60);
		tFPrecoJogo.setMaxWidth(60);
		
		cBPublicadoraJogo.setMaxWidth(130);
		cBPublicadoraJogo.setPrefWidth(130);
		cBDesenvolvedoraJogo.setMaxWidth(130);
		cBDesenvolvedoraJogo.setPrefWidth(130);
		tAJogo.setPrefWidth(390);
		tAJogo.setMaxWidth(390);
		tAJogo.setPrefHeight(195);
		tAJogo.setMaxHeight(195);	
		tAJogo.setEditable(false);
		lVGeneroJogo.setPrefWidth(275);
		lVGeneroJogo.setMaxWidth(275);
		lVGeneroJogo.setPrefHeight(55);
		lVGeneroJogo.setMaxHeight(55);	
		
		tFPrecoJogo.textProperty().addListener((observable, oldValue, newValue) -> {
		    if (!newValue.matches("[0-9]*\\.?[0-9]*")) {
		    	tFPrecoJogo.setText(oldValue);
		    }
		});
		
		Button btnPesquisar = new Button("Pesquisar");
		Button btnInserir = new Button("Inserir");
		Button btnExcluir = new Button("Excluir");
		Button btnAlterar = new Button("Alterar");
		Button btnListar = new Button("Listar");
		Button btnFiltrar = new Button("Filtrar");
		btnPesquisar.setPrefWidth(130);
		btnInserir.setPrefWidth(95);
		btnExcluir.setPrefWidth(95);
		btnAlterar.setPrefWidth(95);
		btnListar.setPrefWidth(95);
		btnFiltrar.setPrefWidth(95);
		
		EventHandler ev = new JogoController(tFIdJogo, tFNomeJogo, tFPrecoJogo,
                tFDataJogo, tAJogo, cBPublicadoraJogo, cBDesenvolvedoraJogo,
                lVGeneroJogo, cBIdJogo, estados);
		try {
		    ((JogoController) ev).atualizarComboIds();
		    carregarDados(); 
		} catch (ClassNotFoundException | SQLException e) {
		    e.printStackTrace();
		}
		btnPesquisar.setOnAction(ev);
		btnInserir.setOnAction(ev);
		btnExcluir.setOnAction(ev);
		btnAlterar.setOnAction(ev);
		btnListar.setOnAction(ev);
		btnFiltrar.setOnAction(ev);
		
		
		HBox priLinha = new HBox(4);
		HBox segLinha = new HBox(3);
		HBox terLinha = new HBox(2);
		HBox quaLinha = new HBox(2);
		HBox quiLinha = new HBox(2);
		HBox sexLinha = new HBox(2);
		HBox setLinha = new HBox(2);
		HBox oitLinha = new HBox(3);
		HBox nonLinha = new HBox(4);
		VBox vBox = new VBox();
		priLinha.getChildren().addAll(labelIdJogo,cBIdJogo, btnPesquisar);
		segLinha.getChildren().addAll(labelIdPesqJogo, new Label("  "), tFIdJogo);
		terLinha.getChildren().addAll(labelNomeJogo, tFNomeJogo);
		quaLinha.getChildren().addAll(labelPrecoJogo, tFPrecoJogo);
		quiLinha.getChildren().addAll(labelDataJogo, tFDataJogo);
		sexLinha.getChildren().addAll(labelPublicadoraJogo, cBPublicadoraJogo);
		setLinha.getChildren().addAll(labelDesenvolvedoraJogo, cBDesenvolvedoraJogo);
		oitLinha.getChildren().addAll(labelGeneroJogo, lVGeneroJogo, btnFiltrar);
		nonLinha.getChildren().addAll(btnInserir, btnExcluir, btnAlterar, btnListar);
		Insets insets = new Insets(25, 12, 12, 125);
		vBox.setPadding(insets);
		vBox.getChildren().addAll(priLinha, segLinha, terLinha, quaLinha, quiLinha, 
				sexLinha,setLinha, oitLinha, nonLinha, tAJogo);
		tab.setContent(vBox);
	}
	
	public void carregarGeneros(ObservableList<Genero> generos) {
		estados.clear();
	    lVGeneroJogo.getItems().setAll(generos);
	    for (Genero g : generos) {
	    	estados.put(g, new SimpleBooleanProperty(false));
        }
    }
	
	public ObservableMap<Genero, BooleanProperty> getEstados() {
        return estados;
    }

    public ListView<Genero> getListView() {
        return lVGeneroJogo;
    }
    
    public void carregarDados() {
    	try {
			GenericDao gDao = new GenericDao();
	        GeneroDao generoDao = new GeneroDao(gDao);
	        List<Genero> generos = generoDao.listar();
	        carregarGeneros(FXCollections.observableArrayList(generos));
	        EmpresaDao empresaDao = new EmpresaDao(gDao);
	        List<Empresa> todasEmpresas = empresaDao.listar();
	        List<Empresa> publicadoras = new ArrayList<>();
	        for (Empresa e : todasEmpresas) {
	            if (e.getTipo().equals("Publicadora") || e.getTipo().equals("Ambas")) {
	                publicadoras.add(e);
	            }
	        }
	        
	        cBPublicadoraJogo.getItems().setAll(publicadoras);
	        List<Empresa> desenvolvedoras = new ArrayList<>();
	        for (Empresa e : todasEmpresas) {
	            if (e.getTipo().equals("Desenvolvedora") || e.getTipo().equals("Ambas")) {
	                desenvolvedoras.add(e);
	            }
	        }
	        cBDesenvolvedoraJogo.getItems().setAll(desenvolvedoras);
	    } catch (SQLException | ClassNotFoundException e) {
	        e.printStackTrace();
	    }
    }
    
    public void reiniciaTabJogo(Tab tab) throws ClassNotFoundException, SQLException {
    	tab.setContent(null);
		adicionarCampos(tab);
	}
}
