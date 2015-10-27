package fileprocessor;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import fileprocessor.controller.CommandLoaderTest;
import fileprocessor.controller.CommandAPITest;
import command.AbsolutePathCommandTest;
import command.FileNameCommandTest;
import command.FolderNameCommandTest;

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
