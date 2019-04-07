package com.github.awvalenti.now.mvc.controller;

import java.io.IOException;

import com.github.awvalenti.now.mvc.model.CodeExecutionService;
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

	private CodeExecutionService service;

	public void setService(CodeExecutionService service) {
		this.service = service;
	}

	@FXML
	private synchronized void codeTyped() {
		Platform.runLater(() -> service.run(txtSourceCode.getText(), this));
	}

	@Override
	public void success(String stdout, String stderr) {
		txtStdout.setText(stdout);
		txtStderr.setText(stderr);
	}

	@Override
	public void cancelled() {
		txtStdout.setText("");
		txtStderr.setText("<PROGRAM HAS FROZEN>");
	}

	@Override
	public void exception(IOException e) {
		txtStdout.setText("");
		txtStderr.setText(e.toString());
	}

}
