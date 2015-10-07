package command;

import java.io.File;

import fileprocessor.model.ICommand;

public class Test2Command implements ICommand {

	@Override
	public String execute() {
		System.out.println("Test 2 working");
		return null;
	}

	@Override
	public void setFile(File file) {
		
	}

}
