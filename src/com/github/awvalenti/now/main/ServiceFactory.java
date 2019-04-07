package com.github.awvalenti.now.main;

import com.github.awvalenti.now.mvc.model.CodeExecutionService;
import com.github.awvalenti.now.mvc.model.Observer;

public class ServiceFactory {

	public static CodeExecutionService create(Observer observer) {
		return new CodeExecutionService();
	}

}
