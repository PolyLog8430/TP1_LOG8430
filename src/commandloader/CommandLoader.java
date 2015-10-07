package commandloader;

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

import fileprocessor.controller.CommandAPI;
import fileprocessor.model.ICommand;

public class CommandLoader extends Thread {
	private ArrayList<Class<? extends ICommand>> commandList;
	private CommandAPI commandAPI;
	
	private WatchService watcher;
	
	
	public CommandLoader(CommandAPI commandAPI, Path path) {
		commandList = new ArrayList<Class<? extends ICommand>>();
		
		try {
			watcher = FileSystems.getDefault().newWatchService();
		    WatchKey key = path.register(watcher,
		                           ENTRY_CREATE,
		                           ENTRY_DELETE,
		                           ENTRY_MODIFY);
		} catch (IOException x) {
		    System.err.println(x);
		}
		
		start();
	}
	
	/**
	 * Load compiled commands from the package command
	 * The paths to the plugin directory must be
	 * added to the project
	 */
	public ArrayList<Class<? extends ICommand>> loadCommands(String path) 
			throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		System.out.println("Current project directory is : " + Paths.get("./").toAbsolutePath());
		
		/* Get plugin directory */
		File commandDirectory;
		if(path == null || path.equals("")) {
			commandDirectory = new File(Paths.get("./").toAbsolutePath() + "/plugin/command");	
		}
		else {
			commandDirectory = new File(path);
		}
		
		if(!commandDirectory.isDirectory()) {
			System.err.println("Specified plugin dir is not a directory");
		}
			
		for (File command : commandDirectory.listFiles()) {
			
			/* For each .class load and add it to the commandList */
			if(command.getName().endsWith(".class")){
				commandList.add(loadCommand(command));
			}
		}
		return commandList;
	}
	
	public Class<? extends ICommand> loadCommand(File command) throws ClassNotFoundException {
		
		String fileName = command.getName().split(".class")[0];
		Class<? extends ICommand> commandClass = (Class<? extends ICommand>) ClassLoader.getSystemClassLoader().loadClass("command." + fileName);
		return commandClass;
	}

	@Override
	public void run() {
		for (;;) {

		    // wait for key to be signaled
		    WatchKey key;
		    try {
		        key = watcher.take();
		    } catch (InterruptedException x) {
		        return;
		    }

		    for (WatchEvent<?> event: key.pollEvents()) {
		        WatchEvent.Kind<?> kind = event.kind();

		        // This key is registered only
		        // for ENTRY_CREATE events,
		        // but an OVERFLOW event can
		        // occur regardless if events
		        // are lost or discarded.
		        if (kind == OVERFLOW) {
		            continue;
		        }
		        else if (kind == ENTRY_CREATE || kind == ENTRY_MODIFY) {
		        	// The filename is the
			        // context of the event.
			        WatchEvent<Path> ev = (WatchEvent<Path>)event;
			        Path fileName = ev.context();
			        
			        for( Class<? extends ICommand> command : commandList) {
			        	if(command.getName().equals(fileName.toString())) {
			        		commandList.remove(command);
			        		try {
								commandList.add(loadCommand(fileName.toFile()));
							} catch (ClassNotFoundException e) {
								e.printStackTrace();
							}
			        		System.out.println("Add to commandAPI");
			        		break;
			        	}
			        }
		        }
		        else if (kind == ENTRY_DELETE) {
		        	WatchEvent<Path> ev = (WatchEvent<Path>)event;
			        Path fileName = ev.context();
			        
			        for( Class<? extends ICommand> command : commandList) {
			        	if(command.getName().equals(fileName.toString())) {
			        		commandList.remove(command);
			        		System.out.println("Remove from commandAPI");
			        	}
			        }
		        }
		    }

		    // Reset the key -- this step is critical if you want to
		    // receive further watch events.  If the key is no longer valid,
		    // the directory is inaccessible so exit the loop.
		    boolean valid = key.reset();
		    if (!valid) {
		        break;
		    }
		}
	}
}
