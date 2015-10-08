package command;

import fileprocessor.model.ICommand;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Test class for FileNameCommand
 * @see FileNameCommand
 */
public class FileNameCommandTest {
    private FileNameCommand command;
    private File test;

    @Before
    public void setUp() throws Exception {
        command = new FileNameCommand();
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
     * Excepted the correct result name
     * @throws Exception
     */
    @Test
    public void testCorrectFile() throws Exception {
        test = File.createTempFile("test","");
        command.setFile(test);
        command.execute();
        assertEquals(ICommand.CommandCodeResult.SUCCESS,command.getCodeResult());
        assertEquals(test.getName(),command.getResult());
    }

    /**
     * Test with a dir
     * Excepted code result ERROR
     * @throws Exception
     */
    @Test
    public void testFailedDir() throws Exception {
        Path dirPath = Files.createTempDirectory("test");
        test = dirPath.toFile();
        command.setFile(test);
        command.execute();
        assertEquals(ICommand.CommandCodeResult.ERROR,command.getCodeResult());
    }

    @After
    public void tearDown() throws Exception {
        if(test != null){
            test.deleteOnExit();
        }
    }
}
