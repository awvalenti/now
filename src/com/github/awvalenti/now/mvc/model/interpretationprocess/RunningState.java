package com.github.awvalenti.now.mvc.model.interpretationprocess;

public class RunningState implements State {

	private final StateFactory states;

	public RunningState(StateFactory states) {
		this.states = states;
		startProcess();
	}

	private void startProcess() {
	}

	@Override
	public State keyTyped() {
		cleanUp();
		return states.debounce();
	}

	private void cleanUp() {
	}

	@Override
	public State processFinished() {
		cleanUp();
		informSuccess();
		return states.idle();
	}

	private void informSuccess() {
	}

	@Override
	public State timeout() {
		cleanUp();
		informFreeze();
		return states.idle();
	}

	private void informFreeze() {
	}

}
