package com.github.awvalenti.now.mvc.model;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.concurrent.atomic.AtomicReference;

public class CodeExecution {

	public static final CodeExecution NULL = new CodeExecution("sort",
			Charset.defaultCharset(), "") {

		@Override
		public boolean hasFinished() {
			return true;
		}

		@Override
		public Result run() {
			return Result.success("", "");
		}

		@Override
		public void stop() {
		}
	};

	public interface Result {
		static Result success(String stdout, String stderr) {
			return o -> o.success(stdout, stderr);
		}

		static Result exception(IOException e) {
			return o -> o.exception(e);
		}

		static Result cancelled() {
			return o -> o.cancelled();
		}

		void inform(Observer o);
	}

	private final String interpreterCommand;
	private final Charset sourceCodeCharset;
	private final String sourceCode;

	private final Process proc;
	private final AtomicReference<State> state = new AtomicReference<>(State.NEW);

	enum State {
		NEW, DECIDED_TO_RUN, DECIDED_TO_STOP, FINISHED
	}

	public CodeExecution(String interpreterCommand, Charset sourceCodeCharset,
			String sourceCode) {
		this.interpreterCommand = interpreterCommand;
		this.sourceCodeCharset = sourceCodeCharset;
		this.sourceCode = sourceCode;
		try {
			proc = Runtime.getRuntime().exec(interpreterCommand);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public Result run() {
		try {
			if (!state.compareAndSet(State.NEW, State.DECIDED_TO_RUN)) {
				return Result.cancelled();
			}

			try (OutputStream stdin = proc.getOutputStream()) {
				stdin.write(sourceCode.getBytes(sourceCodeCharset));
			}

			byte[] buffer = new byte[32768];

			String stdout = readStream(buffer, proc.getInputStream());
			String stderr = readStream(buffer, proc.getErrorStream());

			return state.get().equals(State.DECIDED_TO_STOP) ? Result.cancelled() :
					Result.success(stdout, stderr);

		} catch (IOException e) {
			e.printStackTrace();
			return Result.exception(e);
		}
	}

	public void stop() {
		if (
				(state.compareAndSet(State.NEW, State.DECIDED_TO_STOP)
				||
				state.compareAndSet(State.NEW, State.DECIDED_TO_STOP))
				&& proc.isAlive()) {
			proc.destroy();
		}
	}

	public boolean hasFinished() {
		return state.get().equals(State.DECIDED_TO_STOP) || !proc.isAlive();
	}

	private String readStream(byte[] buffer, InputStream inputStream)
			throws IOException {
		try (InputStream is = inputStream) {
			int readCount = is.read(buffer);
			return readCount <= 0 ? "" :
					new String(buffer, 0, readCount, sourceCodeCharset);
		}
	}

	@Override
	public String toString() {
		return sourceCode;
	}

}
