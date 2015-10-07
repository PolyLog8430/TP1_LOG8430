package fileprocessor.model;

import java.io.File;

public class AbsolutePathCommand implements ICommand{

	private File file;

	public static String getCommandName() {
		return "Chemin absolu";
	}

	@Override
	public String execute() {
		return file.getAbsolutePath();
	}

	@Override
	public void setFile(File file) {
		  this.file = file;
	}
}
