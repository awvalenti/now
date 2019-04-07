package com.github.awvalenti.now.mvc.model;

public class CodeExecutionService {

	private final CodeExecutionOrganizer codeExecutionOrganizer = new CodeExecutionOrganizer(0);

	public void run(String currentSourceCode, Observer observer) {
		codeExecutionOrganizer.sourceCodeArrived(currentSourceCode, observer);
	}

}
