package fileprocessor.controller;

import static org.junit.Assert.*;

import fileprocessor.model.FileNameCommand;
import fileprocessor.model.ICommand;
import org.junit.Test;

import java.io.File;
import java.util.Observable;
import java.util.Observer;

/**
 *
 */
public class CommandAPITest {

	/**
	 * Test to add a command in the queue
	 * Excepted a ICommand.CommandCodeResult.SUCCESS in the observer
	 * @throws Exception
	 */
	@Test
	public void testAddCommandToQueue() throws Exception {
		CommandAPI commandAPI = CommandAPI.getInstance();

		final File test = File.createTempFile("test","");
		commandAPI.addCommandToQueue(FileNameCommand.getCommandName(), test.getPath(), new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				if(o instanceof ICommand){
					ICommand command = (ICommand) o;
					assertEquals(test.getName(),command.getResult());
					assertEquals(ICommand.CommandCodeResult.SUCCESS, command.getCodeResult());
				}
			}
		});
		test.deleteOnExit();
	}

	/**
	 * Test unknown command to the queue
	 * Excepted an Exception
	 * @throws Exception
	 */
	@Test(expected=Exception.class)
	public void testAddUnknownCommandToQueue() throws Exception {
		CommandAPI commandAPI = CommandAPI.getInstance();

		commandAPI.addCommandToQueue("", "", new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				fail();
			}
		});
	}

}
