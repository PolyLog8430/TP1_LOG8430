package fileprocessor.model;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import org.junit.Test;

import fileprocessor.model.FileNameCommand;
import fileprocessor.model.ICommand;
/**
 * Test Cases for the FileNameCommand class.
 * @author Dalia-ATTIA
 *
 */
public class FileNameCommandTest {

	/**
	 * To test the return of the correct command name.
	 * this method is trivial for the current implementation,
	 * the test is more useful for a more complicated system.
	 *  
	 */
	@Test
	public void testGetCommandName() {
		String commandName = FileNameCommand.getCommandName();
		assertEquals("Wrong command name.", "Nom du fichier", commandName);
	}

	/**
	 * Test FileNameCommand execution on folders.
	 */
	@Test
	public void testExecuteForFolder() {
		ICommand fileNameCommand = new FileNameCommand();
		String path = Paths.get("./").toAbsolutePath().toString();
		File folder = new File(path);
		fileNameCommand.setFile(folder);
		assertEquals("FileNameCommand should not execute on folders.", "", fileNameCommand.execute());
	}

	/**
	 * Test FileNameCommand execution on files.
	 */
	@Test
	public void testExecuteForFile() {
		ICommand fileNameCommand = new FileNameCommand();
		String fileName = "NewFileName.txt";
		String path = Paths.get("./").toAbsolutePath().toString();
		path = path.substring(0, path.length()-1) + fileName;

		File testFile = new File(path);
		try {
			testFile.createNewFile();
			fileNameCommand.setFile(testFile);
			assertEquals("FileNameCommand Wrong execution on files.", fileName, fileNameCommand.execute());
			testFile.delete();
		} catch (IOException e) {
			e.printStackTrace();
			fail("TestExecuteForFiles did not run, error in pre-processing");
		}
		
	}

	/**
	 * Test whether FileNameCommand should work for folders.
	 */
	@Test
	public void testIsCommandForFolders() {
		ICommand fileNameCommand = new FileNameCommand();
		assertFalse(fileNameCommand.isCommandForFolders());
	}
	@Test
	/**
	 * Test whether FileNameCommand should work for files.
	 */
	public void testIsCommandForFiles() {
		ICommand fileNameCommand = new FileNameCommand();
		assertTrue(fileNameCommand.isCommandForFiles());
	}
}
