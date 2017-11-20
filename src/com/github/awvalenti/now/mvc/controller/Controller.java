package com.github.awvalenti.now.mvc.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.github.awvalenti.now.mvc.model.Model;
import com.github.awvalenti.now.mvc.model.Observer;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

public class Controller implements Observer, Initializable {

	@FXML
	private TextArea txtSourceCode;
	@FXML
	private TextArea txtStdout;
	@FXML
	private TextArea txtStderr;

	private Model model;

	private String lastSourceCode;

	public void setModel(Model model) {
		this.model = model;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		lastSourceCode = txtSourceCode.getText();
	}

	@FXML
	private synchronized void codeTyped() {
		String currentSourceCode = txtSourceCode.getText();

		if (currentSourceCode.equals(lastSourceCode)) return;

		Platform.runLater(() -> model.run(currentSourceCode));

		lastSourceCode = currentSourceCode;
	}

	@Override
	public void outputProduced(String stdout, String stderr) {
		txtStdout.setText(stdout);
		txtStderr.setText(stderr);
	}

}
