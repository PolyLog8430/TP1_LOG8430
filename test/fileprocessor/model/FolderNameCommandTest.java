package fileprocessor.model;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import org.junit.Test;

import fileprocessor.model.FolderNameCommand;
import fileprocessor.model.ICommand;
/**
 * Test cases for the FolderNameCommand class.
 * @author Dalia-ATTIA
 *
 */
public class FolderNameCommandTest {
	/**
	 * To test the return of the correct command name.
	 * this method is trivial for the current implementation,
	 * the test is more useful for a more complicated system.
	 *  
	 */
	@Test
	public void testGetCommandName() {
		String commandName = FolderNameCommand.getCommandName();
		assertEquals("Wrong command name.", "Nom du dossier", commandName);
	}

	/**
	 * Test FileNameCommand execution on folders.
	 */
	@Test
	public void testExecuteForFolder() {
		ICommand folderNameCommand = new FolderNameCommand();
		String path = Paths.get("./").toAbsolutePath().toString();
		File folder = new File(path);
		folderNameCommand.setFile(folder);
		String folderName = folder.getName();
		assertEquals("FolderNameCommand wrong execution on folder.", folderName, folderNameCommand.execute());
	}

	/**
	 * Test FolderNameCommand execution on files.
	 */
	@Test
	public void testExecuteForFile() {
		ICommand folderNameCommand = new FolderNameCommand();
		String fileName = "NewFileName";
		String path = Paths.get("./").toAbsolutePath().toString() + "\\" + fileName;

		File testFile = new File(path);
		try {
			testFile.createNewFile();
			folderNameCommand.setFile(testFile);
			assertEquals("FolderNameCommand should not execute on files", "", folderNameCommand.execute());
			testFile.delete();
		} catch (IOException ioe) {
			ioe.printStackTrace();
			fail("TestExecuteForFiles did not run, error in pre-processing");
		}
	}

	/**
	 * Test whether FileNameCommand should work for folders.
	 */
	@Test
	public void testIsCommandForFolders() {
		ICommand folderNameCommand = new FolderNameCommand();
		assertTrue(folderNameCommand.isCommandForFolders());
	}
	@Test
	/**
	 * Test whether FileNameCommand should work for files.
	 */
	public void testIsCommandForFiles() {
		ICommand folderNameCommand = new FolderNameCommand();
		assertFalse(folderNameCommand.isCommandForFiles());
	}
}
