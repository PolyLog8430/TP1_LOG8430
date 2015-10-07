package fileprocessor;

import fileprocessor.model.AbsolutePathCommandTest;
import fileprocessor.model.FileNameCommandTest;
import fileprocessor.model.FolderNameCommandTest;
import fileprocessor.view.CommandPanelTest;
import fileprocessor.view.FilePanelTest;
import fileprocessor.view.PolyFilesUITest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import fileprocessor.controller.CommandAPITest;
@RunWith(Suite.class)
@SuiteClasses({CommandAPITest.class, AbsolutePathCommandTest.class,
	FileNameCommandTest.class, FolderNameCommandTest.class, CommandPanelTest.class,
	FilePanelTest.class, PolyFilesUITest.class})
public class AllTests {

}
