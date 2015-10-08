package fileprocessor.controller;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.ArrayList;

import fileprocessor.model.ICommand;
import fileprocessor.model.MetaCommand;

public class CommandAPI extends Observable {
	private CommandLoader commandLoader;
	private Map<MetaCommand, Class<? extends ICommand>> commands = new ConcurrentHashMap<>();
	private Queue<ICommand> commandQueue = new ConcurrentLinkedQueue<>();
	private boolean invokerRunning;
	private static final Object MUTEX_THREAD = new Object();
	private static final Object MUTEX_COMMANDS = new Object();

	public CommandAPI() {
		 InvokerThread thread = new InvokerThread();
		 invokerRunning = true;
		 thread.start();
		 
		 commandLoader = new CommandLoader(this, null);
		 commandLoader.start();
	}

	public Set<MetaCommand> getCommands() {
		Set<MetaCommand> commandList;
		synchronized (MUTEX_COMMANDS){
			commandList = commands.keySet();
		}
		return commandList;
	}

	public void addCommandToQueue(MetaCommand commandName, String path, Observer response) throws Exception {
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

	public void addCommandClass(MetaCommand commandName, Class<? extends ICommand> commandClass){
		System.out.println("Add command : "+commandName );

		synchronized (MUTEX_COMMANDS){
			commands.put(commandName, commandClass);
			this.setChanged();
			this.notifyObservers();
		}
	}

	public void removeCommandClass(MetaCommand commandName) throws Exception {
		System.out.println("Remove command : "+commandName );

		synchronized (MUTEX_COMMANDS){
			if(commands.containsKey(commandName)){
				commands.remove(commandName);
			}
			else{
				throw new Exception("No command to remove");
			}

			this.setChanged();
			this.notifyObservers();
		}
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

	public CommandLoader getCommandLoader() {
		return commandLoader;
	}
	
	
}
