package fileprocessor.view;

import org.junit.Test;

import fileprocessor.controller.CommandDataParser;
import fileprocessor.model.MetaCommand;

public class CommandDataParserTest {

	@Test
	public void test() {
		
		CommandDataParser cdp = new CommandDataParser();
		MetaCommand mc1 = cdp.generateMetacommand("bin/command/AbsolutePathCommand.xml");
		assert(mc1.getName().equals("File Name Command"));
		
		assert(!mc1.isApplyOnFile());
		
		assert(mc1.isApplyOnFolder());
		
	}

}
