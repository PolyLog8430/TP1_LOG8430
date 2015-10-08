package fileprocessor.controller;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import fileprocessor.model.ICommand;
import fileprocessor.model.MetaCommand;

/**
 * Controller class
 * Notify when the list of commands class change
 */
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

	/**
	 * @return List of MetaCommand updated
	 */
	public Set<MetaCommand> getCommands() {
		Set<MetaCommand> commandList;
		synchronized (MUTEX_COMMANDS){
			commandList = commands.keySet();
		}
		return commandList;
	}

	/**
	 * Add command to thread queue
	 * @param commandName the MetaCommand class
	 * @param path Path of file/folder selected
	 * @param response Observer to notify the result of command
	 * @throws Exception
	 */
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

	/**
	 * Add MetaCommand and Class file to the list
	 * @param commandName the MetaCommand class
	 * @param commandClass the Class which extend ICommand
	 */
	public void addCommandClass(MetaCommand commandName, Class<? extends ICommand> commandClass){
		System.out.println("Add command : "+commandName );

		synchronized (MUTEX_COMMANDS){
			commands.put(commandName, commandClass);
			this.setChanged();
			this.notifyObservers();
		}
	}

	/**
	 * Remove MetaCommand
	 * @param commandName the MetaCommand to remove
	 * @throws Exception
	 */
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

	/**
	 * Execute the first command of the queue
	 */
	private void executeCommand(){
		if(!commandQueue.isEmpty()){
			ICommand cmdToExecute = commandQueue.poll();
			cmdToExecute.execute();
		}
	}

	/**
	 * Test if the invoker thread is running
	 * @return boolean
	 */
	public synchronized boolean isInvokerRunning(){
		return invokerRunning;
	}

	/**
	 * InvokerThread to handle the command queue
	 */
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
