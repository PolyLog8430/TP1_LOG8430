package command;

import java.io.File;

import fileprocessor.model.ICommand;

public class Test2Command extends ICommand {

	@Override
	public void execute() {
		System.out.println("Test 2 working");
	}

	@Override
	public void setFile(File file) {
		
	}

}
