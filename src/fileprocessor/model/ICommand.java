package fileprocessor.model;

import java.io.File;
import java.util.Observable;

public abstract class ICommand extends Observable{

	public enum CommandCodeResult{SUCCESS, ERROR}
	protected File file;
	protected String result = "";
	protected CommandCodeResult codeResult = CommandCodeResult.SUCCESS;

	public abstract void execute();

	public void setFile(File file) {
		this.file = file;

	}
	public String getResult(){
		return result;
	}
	public CommandCodeResult getCodeResult(){
		return codeResult;
	}
}
