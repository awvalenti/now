package com.github.awvalenti.nexo.mvc.controller;

import com.github.awvalenti.nexo.mvc.model.Model;
import com.github.awvalenti.nexo.mvc.model.Observer;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class Controller implements Observer {

	@FXML
	private TextArea txtSourceCode;
	@FXML
	private TextArea txtStdout;
	@FXML
	private TextArea txtStderr;

	private Model model;

	public void setModel(Model model) {
		this.model = model;
	}

	@FXML
	private void codeTyped() {
		Platform.runLater(() -> {
			new Thread(() -> model.run(txtSourceCode.getText())).start();
		});
	}

	@Override
	public void outputProduced(String stdout, String stderr) {
		txtStdout.setText(stdout);
		txtStderr.setText(stderr);
	}

}
