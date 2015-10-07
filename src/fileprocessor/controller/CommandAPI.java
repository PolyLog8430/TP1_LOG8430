package fileprocessor.controller;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.ArrayList;

import fileprocessor.model.AbsolutePathCommand;
import fileprocessor.model.FileNameCommand;
import fileprocessor.model.FolderNameCommand;
import fileprocessor.model.ICommand;

public class CommandAPI  {
	private Map<String, Class<? extends ICommand>> commands;
	private Queue<ICommand> commandQueue;
	private boolean invokerRunning;
	private InvokerThread thread;
	private static final Object MUTEX_THREAD = new Object();
	
	//Singleton
	private static CommandAPI instance = null;
    public static CommandAPI getInstance() {
       if(instance == null) {
          instance = new CommandAPI();
       }
       return instance;
    }

	protected CommandAPI() {
		 commands = new HashMap<String, Class<? extends ICommand>>();
		 commandQueue = new ConcurrentLinkedQueue< ICommand>();

		 thread = new InvokerThread();
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

	public void addCommandToQueue(String commandName, String path) throws Exception, InstantiationException, IllegalAccessException {
		if (commands.containsKey(commandName)) {
			ICommand command = commands.get(commandName).newInstance();	
			command.setFile(new File(path));
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

	private synchronized boolean isInvokerRunning(){
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
