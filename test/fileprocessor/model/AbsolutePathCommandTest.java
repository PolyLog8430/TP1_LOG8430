package fileprocessor.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import org.junit.Test;
/**
 * Test Cases for the AbsolutePathCommand class.
 * @author Dalia-ATTIA
 *
 */
public class AbsolutePathCommandTest {

	/**
	 * To test the return of the correct command name.
	 * this method is trivial for the current implementation,
	 * the test is more useful for a more complicated system.
	 *  
	 */
	@Test
	public void testGetCommandName() {
		String commandName = AbsolutePathCommand.getCommandName();
		assertEquals("Wrong command name.", "Chemin absolu", commandName);
	}

	/**
	 * Test the execution of the command on folders.
	 */
	@Test
	public void testExecuteForFolder() {
		ICommand absolutePathCommand = new AbsolutePathCommand();
		String path = Paths.get("./").toAbsolutePath().toString();
		File folder = new File(path);
		String folderPath = folder.getAbsolutePath();
		absolutePathCommand.setFile(folder);
		assertEquals("Execute AbsolutePathCommand for folders fails", 
				folderPath, absolutePathCommand.execute());
	}
	/**
	 * Test the execution of the command on files.
	 */
	@Test
	public void testExecuteForFile() {
		ICommand absolutePathCommand = new AbsolutePathCommand();
		String path = Paths.get("./").toAbsolutePath().toString() + "\\NewFileName.txt";
		try {
			File testFile = new File(path);
			testFile.createNewFile();
			absolutePathCommand.setFile(testFile);
			assertEquals("Execute AbsolutePathCommand for files fails", 
					path, absolutePathCommand.execute());
			testFile.delete();
		} catch (IOException e) {
			e.printStackTrace();
			fail("TestExecuteForFiles did not run, error in pre-processing");
		}
	}

	/**
	 * Test whether this command should work for folders.
	 */
	@Test
	public void testIsCommandForFolders() {
		ICommand absolutePathCommand = new AbsolutePathCommand();
		assertTrue(absolutePathCommand.isCommandForFolders());
	}

	/**
	 * Test whether this command should work for files.
	 */
	@Test
	public void testIsCommandForFiles() {
		ICommand absolutePathCommand = new AbsolutePathCommand();
		assertTrue(absolutePathCommand.isCommandForFiles());
	}
}
