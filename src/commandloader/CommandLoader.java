package commandloader;

import java.io.File;
import java.nio.file.Paths;
import java.util.LinkedList;

import fileprocessor.model.ICommand;

public class CommandLoader {
	LinkedList<Class<? extends ICommand>> commandList;
	
	public CommandLoader() {
		commandList = new LinkedList<Class<? extends ICommand>>();
	}
	
	/**
	 * Load commands from the package command
	 * Commands needs to be compiled and the paths to plugin directory added to the project
	 * 
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public void loadCommands() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		System.out.println("Current project directory is : " + Paths.get("./").toAbsolutePath());
		File commandDirectory = new File(Paths.get("./").toAbsolutePath() + "/plugin/command");
		for (File command : commandDirectory.listFiles()) {
			
			if(command.getName().endsWith(".class")){
				String fileName = command.getName().split(".class")[0];
				Class<? extends ICommand> commandClass = (Class<? extends ICommand>) ClassLoader.getSystemClassLoader().loadClass("command." + fileName);
				commandList.add(commandClass);
				ICommand testInstance = commandClass.newInstance();
				testInstance.execute();
			}
		}
	}

	public LinkedList<Class<? extends ICommand>> getCommandList() {
		return commandList;
	}
	
}
