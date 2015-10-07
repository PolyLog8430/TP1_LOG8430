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
	 * Load compiled commands from the package command
	 * The paths to the plugin directory must be
	 * added to the project
	 */
	public void loadCommands() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		System.out.println("Current project directory is : " + Paths.get("./").toAbsolutePath());
		/* Get plugin directory */
		File commandDirectory = new File(Paths.get("./").toAbsolutePath() + "/plugin/command");
		for (File command : commandDirectory.listFiles()) {
			
			/* For each .class load and add it to the commandList */
			if(command.getName().endsWith(".class")){
				String fileName = command.getName().split(".class")[0];
				Class<? extends ICommand> commandClass = (Class<? extends ICommand>) ClassLoader.getSystemClassLoader().loadClass("command." + fileName);
				commandList.add(commandClass);
			}
		}
	}
	
	/**
	 * Getter used from CommandAPI
	 */
	public LinkedList<Class<? extends ICommand>> getCommandList() {
		return commandList;
	}
}
