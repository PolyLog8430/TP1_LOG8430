package fileprocessor.model;

import java.io.File;

public interface ICommand {
	String execute();
	void setFile(File file);

}
