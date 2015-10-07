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
	
	//Singleton
	private static CommandAPI instance = null;
    public synchronized static CommandAPI getInstance() {
       if(instance == null) {
          instance = new CommandAPI();
       }
       return instance;
    }

	protected CommandAPI() {
		 commands = new HashMap<String, Class<? extends ICommand>>();
		 commandQueue = new ConcurrentLinkedQueue< ICommand>();

		 InvokerThread thread = new InvokerThread();
		 invokerRunning = true;
		 thread.start();

		 loadCommands();
	}
	
	public ArrayList<String> getCommands() {
		ArrayList<String> commandList = new ArrayList<String>();
		for(String key : commands.keySet()) {
			commandList.add(key);
		}
		return commandList;
	}

	public void addCommandToQueue(String commandName, String path, Observer response) throws Exception, InstantiationException, IllegalAccessException {
		if (commands.containsKey(commandName)) {
			// Init command
			ICommand command = commands.get(commandName).newInstance();
			command.setFile(new File(path));
			command.addObserver(response);

			commandQueue.add(command);

            // Wake up the invoker thread
            synchronized (MUTEX_THREAD){
				MUTEX_THREAD.notifyAll();
			}
		} else {
			throw new Exception("Command does not exist.");
		}
		
	}
	
	private void loadCommands() {
		commands.put(FileNameCommand.getCommandName(), FileNameCommand.class);
		commands.put(FolderNameCommand.getCommandName(), FolderNameCommand.class);
		commands.put(AbsolutePathCommand.getCommandName(), AbsolutePathCommand.class);
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
