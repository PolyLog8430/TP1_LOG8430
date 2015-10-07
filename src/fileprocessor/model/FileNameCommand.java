package fileprocessor.model;

public class FileNameCommand extends ICommand {

    public static String getCommandName() {
        return "Nom du fichier";
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
