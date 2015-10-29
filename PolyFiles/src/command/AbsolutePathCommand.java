package command;

import fileprocessor.model.ICommand;
import fileprocessor.model.MetaCommand;

public class AbsolutePathCommand extends ICommand {
	
	private static final MetaCommand meta = new MetaCommand("Chemin absolu", true,true);
	public static MetaCommand getCommandName() {
		return meta;
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
