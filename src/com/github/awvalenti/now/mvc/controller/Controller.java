package com.github.awvalenti.now.mvc.controller;

import com.github.awvalenti.now.mvc.model.Model;
import com.github.awvalenti.now.mvc.model.Observer;

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
			model.run(txtSourceCode.getText());
		});
	}

	@Override
	public void outputProduced(String stdout, String stderr) {
		txtStdout.setText(stdout);
		txtStderr.setText(stderr);
	}

}
