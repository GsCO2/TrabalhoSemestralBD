package application;
	
import java.sql.SQLException;

import application.controller.tab.BibliotecaTabController;
import application.controller.tab.EmpresaTabController;
import application.controller.tab.GeneroTabController;
import application.controller.tab.JogoTabController;
import application.controller.tab.UsuarioTabController;
import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;


public class Main extends Application {
	
	@Override
	public void start(Stage primaryStage) {
		try {
			TabPane tabPane = new TabPane();
			
			Tab tabJogo = new Tab("Jogo", new Label("Jogo"));
			Tab tabUsuario = new Tab("Usuario", new Label("Usuario"));
			Tab tabGenero = new Tab("Genero", new Label("Genero"));
			Tab tabEmpresa = new Tab("Empresa", new Label("Empresa"));
			Tab tabBiblioteca = new Tab("Biblioteca", new Label("Biblioteca"));
			
			tabPane.getTabs().add(tabUsuario);
			tabPane.getTabs().add(tabGenero);
			tabPane.getTabs().add(tabEmpresa);
			tabPane.getTabs().add(tabJogo);
			tabPane.getTabs().add(tabBiblioteca);
			
			GeneroTabController generoTabController = new GeneroTabController();
			generoTabController.adicionarCampos(tabGenero);
			JogoTabController jogoTabController = new JogoTabController();
			jogoTabController.adicionarCampos(tabJogo);
			UsuarioTabController usuarioTabController = new UsuarioTabController();
			usuarioTabController.adicionarCampos(tabUsuario);
			EmpresaTabController empresaTabController = new EmpresaTabController();
			empresaTabController.adicionarCampos(tabEmpresa);
			BibliotecaTabController bibliotecaTabController = new BibliotecaTabController();
			bibliotecaTabController.adicionarCampos(tabBiblioteca);
			
			tabBiblioteca.setOnSelectionChanged((EventHandler<Event>) event -> {
			    if(tabBiblioteca.isSelected()) {
			        try {
			            bibliotecaTabController.reiniciaTabBiblioteca(tabBiblioteca);
			        } catch (ClassNotFoundException | SQLException e) {
			            e.printStackTrace();
			        }
			    }
			});
			
			tabEmpresa.setOnSelectionChanged((EventHandler<Event>) event -> {
			    if(tabEmpresa.isSelected()) {
			        try {
			            empresaTabController.reiniciaTabEmpresa(tabEmpresa);
			        } catch (ClassNotFoundException | SQLException e) {
			            e.printStackTrace();
			        }
			    }
			});
			
			tabGenero.setOnSelectionChanged((EventHandler<Event>) event -> {
			    if(tabGenero.isSelected()) {
			        try {
			            generoTabController.reiniciaTabGenero(tabGenero);
			        } catch (ClassNotFoundException | SQLException e) {
			            e.printStackTrace();
			        }
			    }
			});
			
			tabUsuario.setOnSelectionChanged((EventHandler<Event>) event -> {
			    if(tabUsuario.isSelected()) {
			        try {
			            usuarioTabController.reiniciaTabUsuario(tabUsuario);
			        } catch (ClassNotFoundException | SQLException e) {
			            e.printStackTrace();
			        }
			    }
			});
			
			tabJogo.setOnSelectionChanged((EventHandler<Event>) event -> {
			    if(tabJogo.isSelected()) {
			        try {
			        	jogoTabController.reiniciaTabJogo(tabJogo);
			        } catch (ClassNotFoundException | SQLException e) {
			            e.printStackTrace();
			        }
			    }
			});
			
			
			VBox vBox = new VBox(tabPane);
			Scene scene = new Scene(vBox, 640, 480);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Sistema de Jogos");
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
