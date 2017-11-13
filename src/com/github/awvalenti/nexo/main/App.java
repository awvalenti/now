package com.github.awvalenti.nexo.main;

import java.nio.charset.Charset;

import com.github.awvalenti.nexo.mvc.controller.Controller;
import com.github.awvalenti.nexo.mvc.model.factory.ModelFactory;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(Charset.forName("US-ASCII"));

		Parent root = loader.load(getClass().getResourceAsStream(
				"/com/github/awvalenti/nexo/mvc/view/View.fxml"));

		Controller controller = loader.getController();
		controller.setModel(ModelFactory.tcc(controller,
				"C:\\desenvolvimento\\ferramentas\\c\\tcc\\win32"));

		primaryStage.setScene(new Scene(root));
		primaryStage.setTitle("Nexo");
		primaryStage.show();
	}

}