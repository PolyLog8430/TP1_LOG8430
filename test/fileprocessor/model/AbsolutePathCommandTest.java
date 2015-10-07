package fileprocessor.model;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Test class for AbsolutePathCommand
 * @see AbsolutePathCommand
 */
public class AbsolutePathCommandTest {

    private AbsolutePathCommand command;
    private File test;

    @Before
    public void setUp() throws Exception {
        command = new AbsolutePathCommand();
    }

    /**
     * Test when the file is null
     * Excepted code result ERROR
     * @throws Exception
     */
    @Test
    public void testNullFile() throws Exception {
        command.setFile(null);
        command.execute();
        assertEquals(ICommand.CommandCodeResult.ERROR,command.getCodeResult());
    }

    /**
     * Test when the file not exist
     * Excepted code result ERROR
     * @throws Exception
     */
    @Test
    public void testNotExsitingFile() throws Exception {
        command.setFile(new File(""));
        command.execute();
        assertEquals(ICommand.CommandCodeResult.ERROR,command.getCodeResult());
    }

    /**
     * Test a correct file
     * Excepted code result SUCCESS
     * Excepted the correct result path
     * @throws Exception
     */
    @Test
    public void testCorrectFile() throws Exception {
        test = File.createTempFile("test","");
        command.setFile(test);
        command.execute();
        assertEquals(ICommand.CommandCodeResult.SUCCESS,command.getCodeResult());
        assertEquals(test.getAbsolutePath(),command.getResult());
    }

    /**
     * Test a correct dir
     * Excepted code result SUCCESS
     * Excepted the correct result dir
     * @throws Exception
     */
    @Test
    public void testCorrectDir() throws Exception {
        Path dirPath = Files.createTempDirectory("test");
        test = dirPath.toFile();
        command.setFile(test);
        command.execute();
        assertEquals(ICommand.CommandCodeResult.SUCCESS,command.getCodeResult());
        assertEquals(test.getAbsolutePath(),command.getResult());
    }

    @After
    public void tearDown() throws Exception {
        if(test != null){
            test.deleteOnExit();
        }
    }
}
