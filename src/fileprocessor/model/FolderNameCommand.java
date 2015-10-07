package fileprocessor.model;

import java.io.File;

public class FolderNameCommand implements ICommand {

	private File file;

	public static String getCommandName() {
		return "Nom du dossier";
	}

	@Override
	public String execute() {
		return this.file.getName();
	}

	@Override
	public void setFile(File file) {
		this.file = file;
	}
}
