package fileprocessor.model;

public class MetaCommand {
	private String name;
	private boolean applyOnFolder;
	private boolean applyOnFile;
	
	public MetaCommand(String name, boolean applyOnFolder, boolean applyOnFile) {
		this.name = name;
		this.applyOnFolder = applyOnFolder;
		this.applyOnFile = applyOnFile;
	}

	public String getName() {
		return name;
	}

	public boolean isApplyOnFolder() {
		return applyOnFolder;
	}

	public boolean isApplyOnFile() {
		return applyOnFile;
	}
}
