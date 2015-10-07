package fileprocessor.controller;

import static org.junit.Assert.*;

import fileprocessor.model.FileNameCommand;
import fileprocessor.model.ICommand;
import org.junit.Test;

import java.io.File;

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
