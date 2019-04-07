package com.github.awvalenti.now.mvc.model.interpretationprocess;

public interface State {

	State keyTyped();

	State processFinished();

	State timeout();

}
