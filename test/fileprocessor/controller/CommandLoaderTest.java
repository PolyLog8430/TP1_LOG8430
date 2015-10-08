package fileprocessor.controller;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.Test;

import fileprocessor.model.ICommand;

public class CommandLoaderTest {

	@Test
	public void test() {
		/*CommandLoader cl = null;
		
		try {
			cl = new CommandLoader("");
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch (InstantiationException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		try {
			ArrayList<Class<? extends ICommand>> commandList = cl.getCommandList();
			for(Class<? extends ICommand> commandClass : commandList) {
				ICommand command =  commandClass.newInstance();
				command.setFile(new File("."));
				command.execute();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		*/
	}

}
