package com.friden.simplethreadmodules.examples;

import java.util.Arrays;

import com.friden.simplethreadmodules.Module;
import com.friden.simplethreadmodules.ModuleConnector;


/**
 * @author friden
 *
 *	This class connects all into a production line that:
 *  * Makes strings of numbers
 *  * Parses them into integers
 *  * Increments them
 *  * Shows them
 *  * And logs them
 *  
 *  This if of course not meant as a good example of what this might
 *  be used for, just as an example of how to make something highly
 *  parallel. 
 *
 */
public class AllConnected {
	
	public static void main(String[] arg){
		
		int numberOfMsgs = 10;
		
		ModuleConnector connector = new ModuleConnector();
		
		Logger logger     = new Logger(numberOfMsgs, connector);
		Shower shower     = new Shower(logger);
		Incrementer incer = new Incrementer(shower);
		Parser parser     = new Parser(incer);
		Maker maker       = new Maker(numberOfMsgs, parser);
		
		connector.addModules(Arrays.asList(new Module[]{logger,shower,incer,parser,maker}));
		connector.startModules();
		
	}
	
}
