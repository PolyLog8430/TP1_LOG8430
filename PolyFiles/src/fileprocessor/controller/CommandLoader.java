package fileprocessor.controller;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.HashMap;

import fileprocessor.model.ICommand;
import fileprocessor.model.MetaCommand;

public class CommandLoader extends Thread {

	private HashMap<MetaCommand, Class<? extends ICommand>> commandList;
	private CommandAPI commandAPI;
	private CommandDataParser commandDataParser;
	private Path commandDirectoryPath;
	private WatchService watcher;

	/**
	 * Initialize the CommandLoader
	 * @param commandAPI Instance of CommandApi necessary to update dynamically commands
	 * @param path Path to the plugin folder or null
	 */
	public CommandLoader(CommandAPI commandAPI, String path) {

		this.commandAPI = commandAPI;
		commandList = new HashMap<MetaCommand, Class<? extends ICommand>>();
		commandDataParser = new CommandDataParser();

		/* Get plugin directory */
		File commandDirectory = null;
		commandDirectoryPath = Paths.get("bin/command/").toAbsolutePath();
		System.out.println("Command Directory Path: " + commandDirectoryPath);

		if (path == null || path.equals("")) {
			commandDirectory = commandDirectoryPath.toFile();
		} else {
			commandDirectory = new File(path);
		}

		if (!commandDirectory.exists()) {
			System.err.println("No such File or Directory");
		}

		if (!commandDirectory.isDirectory()) {
			System.err.println("Specified plugin dir is not a directory");
		}

		try {
			watcher = FileSystems.getDefault().newWatchService();
		} catch (IOException e) {

			e.printStackTrace();
		}
		try {
			WatchKey key = commandDirectoryPath.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			initLoadCommands(commandDirectory);
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Load compiled commands from the package command The paths to the plugin
	 * directory must be added to the project
	 */
	public void initLoadCommands(File commandDirectory)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		System.out.println("Current project directory is : " + commandDirectory.getAbsolutePath());

		for (File command : commandDirectory.listFiles()) {

			/* For each .class load and add it to the commandList */
			if (command.getName().endsWith(".class")) {
				Class<? extends ICommand> commandClass = loadCommand(command);
				MetaCommand mc = commandDataParser.generateMetacommand(command.getAbsolutePath().split(".class")[0] + ".xml");
				commandList.put(mc, commandClass);
				commandAPI.addCommandClass(mc, commandClass);
			}
		}
	}

	/**
	 * Load a command from .class file 
	 * @param command
	 * @return
	 * @throws ClassNotFoundException
	 */
	public Class<? extends ICommand> loadCommand(File command) throws ClassNotFoundException {

		String fileName = command.getName().split(".class")[0];
		Class<? extends ICommand> commandClass = (Class<? extends ICommand>) ClassLoader.getSystemClassLoader()
				.loadClass("command." + fileName);
		return commandClass;
	}

	/**
	 * Plugin directory listener loop
	 * Check if a file is added or removed from 
	 * the Plugin directory
	 * In case it is a .class file add or remove
	 * it from the commandList and commandApi list
	 */
	@Override
	public synchronized void run() {
		for (;;) {
			WatchKey key = null;
			// wait for key to be signaled
			try {
				key = watcher.take();
			} catch (InterruptedException x) {
				return;
			}
			System.out.println("Plugin directory updated !");
			for (WatchEvent<?> event : key.pollEvents()) {
				WatchEvent.Kind<?> kind = event.kind();

				// This key is registered only
				// for ENTRY_CREATE events,
				// but an OVERFLOW event can
				// occur regardless if events
				// are lost or discarded.
				if (kind == OVERFLOW) {
					continue;
					// Ajouter kind == ENTRY_MODIFY
				} else if (kind == ENTRY_CREATE ) {
					// The filename is the
					// context of the event.
					WatchEvent<Path> ev = (WatchEvent<Path>) event;
					Path fileName = ev.context();

					if(!fileName.toString().endsWith(".class")) {
						continue;
					}
					System.out.println("Add new Command to CommandAPI : " + fileName.toString());
					
					try {
						Class<? extends ICommand> commandClass = loadCommand(fileName.toFile());
						String metaCommandPath = commandDirectoryPath.toString() + "/" 
								+ fileName.toString().split(".class")[0] + ".xml";
						MetaCommand mc = commandDataParser.generateMetacommand(metaCommandPath);
						
						commandList.put(mc, commandClass);
						commandAPI.addCommandClass(mc, commandClass);
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}				
					
				} else if (kind == ENTRY_DELETE) {
					WatchEvent<Path> ev = (WatchEvent<Path>) event;
					Path fileName = ev.context();

					if(!fileName.toString().endsWith(".class")) {
						continue;
					}

					for(MetaCommand metaCommand: commandList.keySet()){
						if (commandList.get(metaCommand).getSimpleName().equals(fileName.toString().split(".class")[0])) {
							System.out.println("Remove Command from commandAPI : " + fileName.toString() );

							try {
								commandAPI.removeCommandClass(metaCommand);
								commandList.remove(metaCommand);
							} catch (Exception e) {
								e.printStackTrace();
							}
							break;
						}
					}
				}
			}

			// Reset the key -- this step is critical if you want to
			// receive further watch events. If the key is no longer valid,
			// the directory is inaccessible so exit the loop.
			boolean valid = key.reset();
			if (!valid) {
				break;
			}
		}
	}


	public HashMap<MetaCommand, Class<? extends ICommand>> getCommandList() {
		return commandList;
	}

}
