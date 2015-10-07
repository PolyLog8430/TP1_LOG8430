package command;

import java.io.File;

import fileprocessor.model.ICommand;


public class TestCommand implements ICommand {

	@Override
	public String execute() {
		System.out.println("Test working");
		return null;
	}

	@Override
	public void setFile(File file) {

	}

}
