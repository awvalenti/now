package com.github.awvalenti.now.mvc.model.interpretationprocess;

public class IdleState implements State {

	private final StateFactory states;

	public IdleState(StateFactory states) {
		this.states = states;
	}

	@Override
	public State keyTyped() {
		return states.debounce();
	}

	@Override
	public State processFinished() {
		throw new UnsupportedOperationException("Incompatible transition");
	}

	@Override
	public State timeout() {
		throw new UnsupportedOperationException("Incompatible transition");
	}

}
