package com.github.awvalenti.now.mvc.model.interpretationprocess;

public class StateFactory {

	public State idle() {
		return new IdleState(this);
	}

	public State debounce() {
		return new DebouncingState(this);
	}

	public State running() {
		return new RunningState(this);
	}

}
