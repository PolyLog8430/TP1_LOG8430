package fileprocessor.view;

import static org.junit.Assert.*;

import org.junit.Test;

import fileprocessor.controller.CommandDataParser;
import fileprocessor.model.MetaCommand;

public class CommandDataParserTest {

	@Test
	public void test() {
		
		CommandDataParser cdp = new CommandDataParser();
		MetaCommand mc1 = cdp.generateMetacommand("/home/template/Documents/LOG8430/TP1_LOG8430/plugin/command/TestCommand.xml");
		MetaCommand mc2 = cdp.generateMetacommand("/home/template/Documents/LOG8430/TP1_LOG8430/plugin/command/TestCommand2.xml");
		assert(mc1.getName().equals("Test Command"));
		assert(mc2.getName().equals("Test Command 2"));
		
		assert(!mc1.isApplyOnFile());
		assert(mc2.isApplyOnFile());
		
		assert(mc1.isApplyOnFolder());
		assert(!mc2.isApplyOnFolder());
		
	}

}
