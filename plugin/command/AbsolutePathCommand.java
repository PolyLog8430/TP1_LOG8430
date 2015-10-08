package command;

import fileprocessor.model.ICommand;
import fileprocessor.model.MetaCommand;

public class AbsolutePathCommand extends ICommand {

	public static MetaCommand getCommandName() {
		return new MetaCommand("Chemin absolu", true,true);
	}

	@Override
	public void execute() {

		if(file != null && file.exists()){
			result = file.getAbsolutePath();
			codeResult = CommandCodeResult.SUCCESS;
		}
		else{
			result = "Erreur : le fichier n'existe pas.";
			codeResult = CommandCodeResult.ERROR;
		}

		this.setChanged();
		this.notifyObservers();
	}
}
