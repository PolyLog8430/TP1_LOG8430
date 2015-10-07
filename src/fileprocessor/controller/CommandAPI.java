package fileprocessor.controller;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import fileprocessor.model.FileNameCommand;
import fileprocessor.model.ICommand;

public class CommandAPI  {
	private Map<String, Class<? extends ICommand>> commands;
	private Queue<ICommand> commandQueue;
	private boolean invokerRunning;
	private InvokerThread thread;

	public CommandAPI() {
		 commands = new HashMap<String, Class<? extends ICommand>>();
		 commandQueue = new ConcurrentLinkedQueue< ICommand>();

		 thread = new InvokerThread();
		 invokerRunning = true;
		 thread.start();

		 loadCommands();
	}

	public void addCommandToQueue(String commandName, String path) throws Exception, InstantiationException, IllegalAccessException {
		if (commands.containsKey(commandName)) {
			ICommand command = commands.get(commandName).newInstance();	
			command.setFile(new File(path));
			commandQueue.add(command);

            // Wake up the invoker thread
            thread.notify();
		} else {
			throw new Exception("Command does not exist.");
		}
		
	}
	
	private void loadCommands() {
		commands.put(FileNameCommand.getCommandName(), FileNameCommand.class);
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
                    try {
                        this.wait();
                    } catch (InterruptedException ignored) {}
                }
			}
		}
	}
}
