package com.github.awvalenti.now.mvc.model.factory;

import java.nio.charset.Charset;

import com.github.awvalenti.now.mvc.model.Model;
import com.github.awvalenti.now.mvc.model.Observer;

public class ModelFactory {

	public static Model forTcc(Observer observer, String tccDirectory) {
		return new Model(tccDirectory + "\\tcc -run -",
				Charset.forName("UTF-8"), observer);
	}

}
