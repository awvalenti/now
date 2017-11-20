package com.github.awvalenti.now.mvc.model;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Avoids repeatedly calling a method by waiting some time before actually
 * calling it. Inspired by _.debounce function from http://underscorejs.org/.
 */
public class Debouncer {

	private final int delay;

	private final Timer timer =
			new Timer(Debouncer.class.getSimpleName(), true);

	private TimerTask currentTask = new TimerTask() {
		@Override
		public void run() {
		}
	};

	public Debouncer(int delay) {
		this.delay = delay;
	}

	public synchronized void run(Runnable newTask) {
		currentTask.cancel();

		currentTask = new TimerTask() {
			private volatile boolean cancelled;

			@Override
			public boolean cancel() {
				cancelled = true;
				return super.cancel();
			}

			@Override
			public void run() {
				synchronized (Debouncer.this) {
					if (!cancelled) newTask.run();
				}
			}
		};

		timer.schedule(currentTask, delay);
	}

}
