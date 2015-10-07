package commandloader;

import static org.junit.Assert.fail;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.junit.Test;

import fileprocessor.controller.CommandAPI;
import fileprocessor.model.ICommand;

public class CommandLoaderTest {

	@Test
	public void test() {
		CommandLoader cl = new CommandLoader(CommandAPI.getInstance(), Paths.get("."));
		try {
			ArrayList<Class<? extends ICommand>> commandList = cl.loadCommands(null);
			for(Class<? extends ICommand> commandClass : commandList) {
				ICommand command =  commandClass.newInstance();
				command.setFile(new File("."));
				command.execute();
			}
			
			Thread.sleep(10000);
			
			commandList = cl.loadCommands(null);
			for(Class<? extends ICommand> commandClass : commandList) {
				ICommand command =  commandClass.newInstance();
				command.setFile(new File("."));
				command.execute();
			}
			
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			fail();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
