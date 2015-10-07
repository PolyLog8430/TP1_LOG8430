package fileprocessor;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import commandloader.CommandLoaderTest;
import fileprocessor.controller.CommandAPITest;
import fileprocessor.model.AbsolutePathCommandTest;
import fileprocessor.model.FileNameCommandTest;
import fileprocessor.model.FolderNameCommandTest;

/**
 * Runner class for all tests.
 * @author Dalia-ATTIA
 *
 */
public class AllTestRunner {
  public static void main(String[] args) {
    Result result = JUnitCore.runClasses(CommandLoaderTest.class, CommandAPITest.class,
    		AbsolutePathCommandTest.class, FileNameCommandTest.class, 
    		FolderNameCommandTest.class);
    for (Failure failure : result.getFailures()) {
      System.out.println(failure.toString());
    }
  }
}
