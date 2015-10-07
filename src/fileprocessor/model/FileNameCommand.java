package fileprocessor.model;

import java.io.File;

public class FileNameCommand implements ICommand {
    private File file;

    public static String getCommandName() {
        return "Nom du Fichier";
    }

    public FileNameCommand(File file) {
        this.file = file;
    }

    @Override
    public String execute() {
        return "Nom du fichier = "+file.getName();
    }

    public void setFile(File file) {
		this.file = file;
    }


}
