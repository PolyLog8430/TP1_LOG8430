package commandloader;

import static org.junit.Assert.fail;

import java.io.File;
import java.util.LinkedList;

import org.junit.Test;

import fileprocessor.model.ICommand;

public class CommandLoaderTest {

	@Test
	public void test() {
		CommandLoader cl = new CommandLoader();
		try {
			LinkedList<Class<? extends ICommand>> commandList = cl.loadCommands(null);
			for(Class<? extends ICommand> commandClass : commandList) {
				ICommand command =  commandClass.newInstance();
				command.setFile(new File("."));
				command.execute();
			}
			
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			fail();
		}
	}

}
