package fileprocessor.model;

public class AbsolutePathCommand extends ICommand {

	public static String getCommandName() {
		return "Chemin absolu";
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
