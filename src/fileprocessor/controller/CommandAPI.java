package fileprocessor.controller;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import fileprocessor.model.FileNameCommand;
import fileprocessor.model.ICommand;

public class CommandAPI  {
	private Map<String, Class<? extends ICommand>> commands;
	private Queue<ICommand> commandQueue;
	public CommandAPI() {
		 commands = new HashMap<String, Class<? extends ICommand>>();
		 commandQueue = new LinkedList< ICommand>();
		 
		 loadCommands();
	}

	public void addCommandToQueue(String commandName, String path) throws Exception, InstantiationException, IllegalAccessException {
		if (commands.containsKey(commandName)) {
			ICommand command = commands.get(commandName).newInstance();	
			command.setFile(new File(path));
			commandQueue.add(command);
		} else {
			throw new Exception("Command does not exist.");
		}
		
	}
	
	private void loadCommands() {
		commands.put(FileNameCommand.getCommandName(), FileNameCommand.class);
	}

}
