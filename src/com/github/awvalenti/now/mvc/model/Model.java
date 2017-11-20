package com.github.awvalenti.now.mvc.model;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

import com.github.awvalenti.now.util.Debouncer;

public class Model {

	private final String interpreterCommand;
	private final Charset sourceCodeCharset;
	private final Observer observer;
	private final Debouncer debouncer;

	public Model(String interpreterCommand, Charset sourceCodeCharset,
			Observer observer, Debouncer debouncer) {
		this.interpreterCommand = interpreterCommand;
		this.sourceCodeCharset = sourceCodeCharset;
		this.observer = observer;
		this.debouncer = debouncer;
	}

	public void run(String sourceCode) {
		debouncer.run(() -> {
			try {
				Process proc = Runtime.getRuntime().exec(interpreterCommand);

				try (OutputStream stdin = proc.getOutputStream()) {
					stdin.write(sourceCode.getBytes(sourceCodeCharset));
				}

				byte[] buffer = new byte[32768];
				String stdout = readStream(buffer, proc.getInputStream());
				String stderr = readStream(buffer, proc.getErrorStream());

				observer.outputProduced(stdout, stderr);

			} catch (IOException e) {
				e.printStackTrace();
			}
		});
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
