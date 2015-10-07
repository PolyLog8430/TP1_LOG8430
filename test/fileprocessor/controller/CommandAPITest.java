package fileprocessor.controller;

import org.junit.Test;

import fileprocessor.model.FileNameCommand;

/**
 * 
 * @author Dalia-ATTIA
 *
 */
public class CommandAPITest {

	@Test
	public void testAddCommandToQueue() {
		CommandAPI commandAPI = new CommandAPI();
		try {
			commandAPI.addCommandToQueue(FileNameCommand.getCommandName(),"test");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
