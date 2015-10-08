package fileprocessor.controller;

import static org.junit.Assert.*;

import command.FileNameCommand;
import fileprocessor.model.ICommand;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Observable;
import java.util.Observer;

/**
 *
 */
public class CommandAPITest {

	private CommandAPI commandAPI;

	@Before
	public void setUp() throws Exception {
		commandAPI = new CommandAPI();
	}

	/**
	 * Test add a command class
	 * Excepted name in getCommandName()
	 * @throws Exception
	 */
	@Test
	public void testAddClassCommand() throws Exception {
		commandAPI.addCommandClass(FileNameCommand.getCommandName(),FileNameCommand.class);
		assertEquals(FileNameCommand.getCommandName(),commandAPI.getCommands().get(0));
	}

	/**
	 * Test remove a command class
	 * Excepted empty commandName
	 * @throws Exception
	 */
	@Test
	public void testRemoveClassCommand() throws Exception {
		commandAPI.addCommandClass(FileNameCommand.getCommandName(),FileNameCommand.class);
		commandAPI.removeCommandClass(FileNameCommand.getCommandName());
		assertEquals(0,commandAPI.getCommands().size());
	}


	/**
	 * Test remove a command class with random name
	 * Excepted Exception
	 * @throws Exception
	 */
	@Test(expected=Exception.class)
	public void testRemoveClassCommandUnknonw() throws Exception {
		commandAPI.addCommandClass(FileNameCommand.getCommandName(),FileNameCommand.class);
		commandAPI.removeCommandClass("toto");
	}

	/**
	 * Test to add a command in the queue
	 * Excepted a ICommand.CommandCodeResult.SUCCESS in the observer
	 * @throws Exception
	 */
	@Test
	public void testAddCommandToQueue() throws Exception {
		final File test = File.createTempFile("test","");
		commandAPI.addCommandClass(FileNameCommand.getCommandName(),FileNameCommand.class);
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
		CommandAPI commandAPI = new CommandAPI();

		commandAPI.addCommandToQueue("", "", new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				fail();
			}
		});
	}

}
