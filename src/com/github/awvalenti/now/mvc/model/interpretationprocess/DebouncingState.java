package com.github.awvalenti.now.mvc.model.interpretationprocess;

public class DebouncingState implements State {

	private final StateFactory states;

	public DebouncingState(StateFactory states) {
		this.states = states;
	}

	@Override
	public State keyTyped() {
		return this;
	}

	@Override
	public State processFinished() {
		throw new UnsupportedOperationException("Incompatible transition");
	}

	@Override
	public State timeout() {
		return states.running();
	}

}
