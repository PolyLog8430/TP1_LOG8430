package fileprocessor.model;

import java.io.File;
import java.util.Observable;

/**
 * Abstract Class for the command
 * Every command must be extends of this class and implements :
 *  - execute()
 *  It provide methods to set File and get results of the command
 */
public abstract class ICommand extends Observable{

	/**
	 * Enum for code result
	 * SUCCESS = command succeeded
	 * ERROR = command have failed
	 */
	public enum CommandCodeResult{SUCCESS, ERROR}

	/**
	 * Current selected file
	 */
	protected File file;

	/**
	 * String result of the command
	 */
	protected String result = "";

	/**
	 * Command code result
	 */
	protected CommandCodeResult codeResult = CommandCodeResult.SUCCESS;

	/**
	 * Work method
	 */
	public abstract void execute();

	/**
	 * Specifiy selected file
	 * @param file File
	 */
	public void setFile(File file) {
		this.file = file;

	}

	/**
	 * @return result of command
	 */
	public String getResult(){
		return result;
	}

	/**
	 * @return code result of command
	 */
	public CommandCodeResult getCodeResult(){
		return codeResult;
	}
}
