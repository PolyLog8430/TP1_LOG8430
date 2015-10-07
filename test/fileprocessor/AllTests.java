package fileprocessor;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import test.controller.CommandAPITest;
import test.model.AbsolutePathCommandTest;
import test.model.FileNameCommandTest;
import test.model.FolderNameCommandTest;
import test.view.CommandPanelTest;
import test.view.FilePanelTest;
import test.view.PolyFilesUITest;

@RunWith(Suite.class)
@SuiteClasses({CommandAPITest.class, AbsolutePathCommandTest.class, 
	FileNameCommandTest.class, FolderNameCommandTest.class, CommandPanelTest.class,
	FilePanelTest.class, PolyFilesUITest.class})
public class AllTests {

}
