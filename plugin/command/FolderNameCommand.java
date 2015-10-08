package command;

import fileprocessor.model.ICommand;
import fileprocessor.model.MetaCommand;

public class FolderNameCommand extends ICommand {

	public static MetaCommand getCommandName() {
		return new MetaCommand("Nom du dossier",true,false);
	}

	@Override
	public void execute() {
		if(file != null && file.exists()){
			if(file.isDirectory()){
				result = file.getName();
				codeResult = CommandCodeResult.SUCCESS;
			}
			else{
				result = "Erreur : " + file.getName() + " n'est pas un dosser.";
				codeResult = CommandCodeResult.ERROR;
			}
		}
		else{
			result = "Erreur : le dossier n'existe pas.";
			codeResult = CommandCodeResult.ERROR;
		}

		this.setChanged();
		this.notifyObservers();
	}
}
