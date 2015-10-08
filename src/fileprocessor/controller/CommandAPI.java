package fileprocessor.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.ArrayList;

import fileprocessor.model.AbsolutePathCommand;
import fileprocessor.model.FileNameCommand;
import fileprocessor.model.FolderNameCommand;
import fileprocessor.model.ICommand;

public class CommandAPI extends Observable {
	private CommandLoader commandLoader;
	private Map<String, Class<? extends ICommand>> commands = new HashMap<>();
	private Queue<ICommand> commandQueue = new ConcurrentLinkedQueue<>();
	private boolean invokerRunning;
	private static final Object MUTEX_THREAD = new Object();
	private static final Object MUTEX_COMMANDS = new Object();

	public CommandAPI() {
		 commands = new HashMap<>();
		 commandQueue = new ConcurrentLinkedQueue<>();

		 InvokerThread thread = new InvokerThread();
		 invokerRunning = true;
		 thread.start();
		 
		 // TODO : importer la liste de commande dynamiquement
		 addCommandClass(FileNameCommand.getCommandName(),FileNameCommand.class);
	}

	public ArrayList<String> getCommands() {
		ArrayList<String> commandList = new ArrayList<String>();
		for(String key : commands.keySet()) {
			commandList.add(key);
		}
		return commandList;
	}

	public void addCommandToQueue(String commandName, String path, Observer response) throws Exception {
		synchronized (MUTEX_COMMANDS){
			if (commands.containsKey(commandName)) {
				// Init command
				ICommand command = commands.get(commandName).newInstance();
				command.setFile(new File(path));
				command.addObserver(response);

				commandQueue.add(command);

			} else {
				throw new Exception("Command does not exist.");
			}
		}

		// Wake up the invoker thread
		synchronized (MUTEX_THREAD){
			MUTEX_THREAD.notifyAll();
		}
	}

	public void addCommandClass(String commandName, Class<? extends ICommand> commandClass){
		synchronized (MUTEX_COMMANDS){
			commands.put(commandName, commandClass);
		}

		 // TODO : informer la vue d'une nouvelle commande
		this.setChanged();
		this.notifyObservers();
	}

	public void removeCommandClass(String commandName) throws Exception {
		synchronized (MUTEX_COMMANDS){
			if(commands.containsKey(commandName)){
				commands.remove(commandName);
			}
			else{
				throw new Exception("No command to remove");
			}
		}

		// TODO : informer la vue d'une nouvelle commande
		this.setChanged();
		this.notifyObservers();
	}

	private void executeCommand(){
		if(!commandQueue.isEmpty()){
			ICommand cmdToExecute = commandQueue.poll();
			cmdToExecute.execute();
		}
	}

	public synchronized boolean isInvokerRunning(){
		return invokerRunning;
	}

	private class InvokerThread extends Thread{

		@Override
		public void run() {
			while (isInvokerRunning()){
				executeCommand();

                if(commandQueue.isEmpty()){
                    synchronized (MUTEX_THREAD){
						try {
							MUTEX_THREAD.wait();
						} catch (InterruptedException ignored) {}
					}
                }
			}
		}
	}
}
