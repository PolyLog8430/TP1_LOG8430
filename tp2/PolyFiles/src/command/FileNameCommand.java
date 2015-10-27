package command;

import fileprocessor.model.ICommand;
import fileprocessor.model.MetaCommand;

public class FileNameCommand extends ICommand {

    public static MetaCommand getCommandName() {
        return new MetaCommand("Nom du fichier", true, false);
    }

    @Override
    public void execute() {
        if(file != null && file.exists()){
            if(file.isFile()){
                result = file.getName();
                codeResult = CommandCodeResult.SUCCESS;
            }
            else{
                result = "Erreur : " + file.getName() + " n'est pas un fichier.";
                codeResult = CommandCodeResult.ERROR;
            }
        }
        else{
            result = "Erreur : le fichier n'exsite pas.";
            codeResult = CommandCodeResult.ERROR;
        }

        this.setChanged();
        this.notifyObservers();
    }
}
