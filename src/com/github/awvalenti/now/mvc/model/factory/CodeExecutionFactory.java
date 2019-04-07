package com.github.awvalenti.now.mvc.model.factory;

import java.nio.charset.Charset;

import com.github.awvalenti.now.mvc.model.CodeExecution;

public class CodeExecutionFactory {

	// XXX
	private static final String tccDirectory;

	static {
		// XXX
		tccDirectory = "C:\\desenvolvimento\\ferramentas\\c\\tcc\\win32";
	}

	public static CodeExecution create(String sourceCode) {
		return new CodeExecution(tccDirectory + "\\tcc -run -",
				Charset.forName("UTF-8"), sourceCode);
	}

}
