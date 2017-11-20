package now;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.github.awvalenti.now.mvc.model.Debouncer;

/**
 * This test makes use of Thread.sleep and tests a class that uses Timer.
 * This is not a best practice, but has helped designing the class. And mocking
 * everything would be a lot of work and probably produce a too much artificial
 * test.
 */
public class DebouncerTest {

	private Debouncer debouncer;
	private StringBuilder output;

	@Before
	public void setUp() {
		debouncer = new Debouncer(10);
		output = new StringBuilder();
	}

	@Test
	public void should_not_run_task_immediately() {
		debouncer.run(() -> output.append("1st"));
		assertThat(output.toString(), is(""));
	}

	@Test
	public void should_run_task_after_delay() {
		debouncer.run(() -> output.append("1st"));
		sleep(30);
		assertThat(output.toString(), is("1st"));
	}

	@Test
	public void should_run_only_the_last_of_a_series_of_tasks() {
		debouncer.run(() -> output.append("1st "));
		debouncer.run(() -> output.append("2nd"));
		sleep(30);
		assertThat(output.toString(), is("2nd"));
	}

	@Test
	public void should_run_all_tasks_if_proper_delay_was_met() {
		debouncer.run(() -> output.append("1st "));
		sleep(30);
		debouncer.run(() -> output.append("2nd"));
		sleep(30);
		assertThat(output.toString(), is("1st 2nd"));
	}

	private void sleep(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

}
