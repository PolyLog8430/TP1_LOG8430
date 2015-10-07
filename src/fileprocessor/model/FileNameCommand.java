package fileprocessor.model;

import java.io.File;

public class FileNameCommand implements ICommand {
    private File file;

    public static String getCommandName() {
        return "Nom du fichier";
    }

    public FileNameCommand() {
    }

    @Override
    public String execute() {
        return file.getName();
    }

    @Override
    public void setFile(File file) {
		this.file = file;
    }


}
