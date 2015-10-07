package fileprocessor.controller;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.ArrayList;

import fileprocessor.model.AbsolutePathCommand;
import fileprocessor.model.FileNameCommand;
import fileprocessor.model.FolderNameCommand;
import fileprocessor.model.ICommand;

public class CommandAPI  {
	private Map<String, Class<? extends ICommand>> commands = new HashMap<>();
	private Queue<ICommand> commandQueue = new ConcurrentLinkedQueue<>();
	private boolean invokerRunning;
	private static final Object MUTEX_THREAD = new Object();
	private static final Object MUTEX_COMMANDS = new Object();

	//Singleton
	private static CommandAPI instance = null;

    public synchronized static CommandAPI getInstance() {
       if(instance == null) {
          instance = new CommandAPI();
       }
       return instance;
    }

	private CommandAPI() {
		 commands = new HashMap<>();
		 commandQueue = new ConcurrentLinkedQueue<>();

		 InvokerThread thread = new InvokerThread();
		 invokerRunning = true;
		 thread.start();
	}
	
	public ArrayList<String> getCommands() {
		ArrayList<String> commandList = new ArrayList<String>();
		for(String key : commands.keySet()) {
			commandList.add(key);
		}
		return commandList;
	}

	public void addCommandToQueue(String commandName, String path, Observer response) throws Exception, InstantiationException, IllegalAccessException {
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
	}

	public  void removeCommandClass(String commandName){
		synchronized (MUTEX_COMMANDS){
			if(commands.containsKey(commandName)){
				commands.remove(commandName);
			}
		}

		// TODO : informer la vue d'une nouvelle commande
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

	public synchronized void setInvokerRunning(boolean isRunning){
		invokerRunning = isRunning;
		synchronized (MUTEX_THREAD){
			MUTEX_THREAD.notifyAll();
		}
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
