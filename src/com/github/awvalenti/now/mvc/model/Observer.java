package com.github.awvalenti.now.mvc.model;

import java.io.IOException;

public interface Observer {

	void success(String stdout, String stderr);

	void cancelled();

	void exception(IOException e);

}
