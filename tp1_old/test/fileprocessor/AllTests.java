package fileprocessor;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import command.AbsolutePathCommandTest;
import command.FileNameCommandTest;
import command.FolderNameCommandTest;

/**
 * Suite classes to run all test cases.
 * @author Dalia-ATTIA
 *
 */
@RunWith(Suite.class)
@SuiteClasses({
	//CommandAPITest.class, 
	AbsolutePathCommandTest.class, 
	FileNameCommandTest.class, FolderNameCommandTest.class,
	//CommandLoaderTest.class
	})
public class AllTests {

}
