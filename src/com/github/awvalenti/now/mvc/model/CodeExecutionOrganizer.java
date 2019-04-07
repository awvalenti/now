package com.github.awvalenti.now.mvc.model;

import java.util.LinkedList;
import java.util.Queue;

import com.github.awvalenti.now.mvc.model.CodeExecution.Result;
import com.github.awvalenti.now.mvc.model.factory.CodeExecutionFactory;

public class CodeExecutionOrganizer {

	private final RunnerThread runner = new RunnerThread();
	private final WatcherThread watcher;

	private static final CodeExecution NULL = CodeExecution.NULL;

	private final long delay;

	// private State state = State.IDLE;
	// private volatile CodeExecution codeExecution;
	private Observer observer;
	public final long timeoutInSeconds = 1;

	public CodeExecutionOrganizer(long delay) {
		this.delay = delay;
		watcher = new WatcherThread(timeoutInSeconds);
		runner.start();
		watcher.start();
	}

	enum State {
		IDLE, DEBOUNCING, RUNNING,;
	}

	public synchronized void sourceCodeArrived(String sourceCode,
			Observer observer) {
		this.observer = observer;
		runner.add(CodeExecutionFactory.create(sourceCode));
	}

	private final class RunnerThread extends Thread {

		private CodeExecution current = NULL;
		private CodeExecution next = NULL;

		public RunnerThread() {
			super("Runner");
			setDaemon(true);
		}

		synchronized void add(CodeExecution codeExecution) {
			current.stop();
			next = codeExecution;
			notify();
		}

		@Override
		public void run() {
			for (;;) {
				waitUntilCodeArrived();
				wakeUpWatcher();
				Result result = current.run();
				current = NULL;
				if (next == NULL) result.inform(observer);
			}
		}

		private synchronized void waitUntilCodeArrived() {
			if (next == NULL) {
				try {
					wait();
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
			}
			current = next;
			next = NULL;
		}

		private void wakeUpWatcher() {
			watcher.add(current);
		}
	}

	private static final class WatcherThread extends Thread {
//		private volatile boolean shouldWait = true;

		private Queue<CodeExecution> q = new LinkedList<>();

		private final long timeoutInSeconds;

		public WatcherThread(long timeoutInSeconds) {
			super("Watcher");
			setDaemon(true);
			this.timeoutInSeconds = timeoutInSeconds;
		}

		public synchronized void add(CodeExecution codeExecution) {
			q.add(codeExecution);
			notify();
//			shouldWait = false;
		}

		@Override
		public void run() {
			try {
				CodeExecution current;
				for (;;) {
					synchronized (this) {
						if (q.isEmpty()) wait();
						while (q.size() > 1) {
							CodeExecution toEliminate = q.remove();
							toEliminate.stop();
						}
						current = q.remove();
					}
					long t0 = System.nanoTime();
					while (!current.hasFinished()) {
						if (System.nanoTime() - t0 < timeoutInSeconds *
								(long) 1e9) {
							Thread.sleep(50);
						} else {
							current.stop();
						}
					}
				}
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
	}

}
