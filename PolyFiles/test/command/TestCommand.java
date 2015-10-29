package command;

import java.io.File;

import fileprocessor.model.ICommand;


public class TestCommand extends ICommand {

	@Override
	public void execute() {
		System.out.println("Test working");
	}

	@Override
	public void setFile(File file) {
		this.file = file;
	}

}
