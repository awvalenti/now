package com.github.awvalenti.now.mvc.model;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

public class Model {

	private final File interpreterDirectory;
	private final String interpreterCommand;
	private final Charset sourceCodeCharset;
	private final Observer observer;

	private String lastSourceCode = "";

	public Model(File interpreterDirectory, String interpreterCommand,
			Charset sourceCodeCharset, Observer observer) {
		this.interpreterDirectory = interpreterDirectory;
		this.interpreterCommand = interpreterCommand;
		this.sourceCodeCharset = sourceCodeCharset;
		this.observer = observer;
	}

	public void run(String sourceCode) {
		if (sourceCode.equals(lastSourceCode)) return;

		lastSourceCode = sourceCode;

		try {
			Process proc = Runtime.getRuntime().exec(interpreterCommand, null,
					interpreterDirectory);

			try (OutputStream outputStream = proc.getOutputStream()) {
				outputStream.write(sourceCode.getBytes(sourceCodeCharset));
			}

			byte[] buffer = new byte[32768];
			String stdout = readStream(buffer, proc.getInputStream());
			String stderr = readStream(buffer, proc.getErrorStream());

			observer.outputProduced(stdout, stderr);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String readStream(byte[] buffer, InputStream inputStream)
			throws IOException {
		try (InputStream is = inputStream) {
			int readCount = is.read(buffer);
			return readCount <= 0 ? "" :
					new String(buffer, 0, readCount, sourceCodeCharset);
		}
	}

}
