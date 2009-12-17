package ar.uba.dc.so.simulator;

import jargs.gnu.CmdLineParser;

public class Simulator {
	public static void main(String[] args) throws Exception {
		CmdLineParser parser = new CmdLineParser();
        CmdLineParser.Option optGraphicMode = parser.addBooleanOption('g', "graphicMode");
        CmdLineParser.Option optMemoryType = parser.addIntegerOption('m', "memoryManager");
        CmdLineParser.Option optMemorySizeInKb = parser.addIntegerOption('s', "memorySize");
        CmdLineParser.Option optRunForInSeconds = parser.addIntegerOption('t', "time");
        CmdLineParser.Option optProcessesFile = parser.addStringOption('p', "processesFile");
        CmdLineParser.Option optFixedPartitionSizeInKb = parser.addIntegerOption('f', "fixedPartitionSize");
        CmdLineParser.Option optPageSizeInKb = parser.addIntegerOption('c', "pageSize");
        
        try {
            parser.parse(args);
        } catch ( CmdLineParser.OptionException e ) {
            System.err.println(e.getMessage());
            CmdLineMode.printHelp();
            System.exit(2);
        }
        
		Boolean graphicMode = (Boolean)parser.getOptionValue(optGraphicMode, Boolean.FALSE);
		
		if (graphicMode) {
			GraphicMode.run();
		} else {
			Integer memoryType = (Integer)parser.getOptionValue(optMemoryType);// Integer.parseInt(args[0]);
			Integer memorySizeInKb = (Integer)parser.getOptionValue(optMemorySizeInKb);
			Integer runForInSeconds = (Integer)parser.getOptionValue(optRunForInSeconds);
			String processesFile = (String)parser.getOptionValue(optProcessesFile);
			Integer fixedPartitionSizeInKb = (Integer)parser.getOptionValue(optFixedPartitionSizeInKb);
			Integer pageSizeInKb = (Integer)parser.getOptionValue(optPageSizeInKb);
			CmdLineMode.run(memoryType, memorySizeInKb, fixedPartitionSizeInKb, pageSizeInKb, runForInSeconds, processesFile);
		}
	}
	

}
