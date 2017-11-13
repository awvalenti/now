package com.github.awvalenti.nexo.mvc.model.factory;

import java.io.File;
import java.nio.charset.Charset;

import com.github.awvalenti.nexo.mvc.model.Model;
import com.github.awvalenti.nexo.mvc.model.Observer;

public class ModelFactory {

	public static Model tcc(Observer observer,
			String tccDirectory) {
		return new Model(new File(tccDirectory), "tcc -run -",
				Charset.forName("US-ASCII"), observer);
	}

}
