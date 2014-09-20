package com.friden.simplethreadmodules.examples;

import com.friden.simplethreadmodules.Data;
import com.friden.simplethreadmodules.DataModule;
import com.friden.simplethreadmodules.ModuleConnector;

public class Logger extends DataModule<Data<String>>{
	
	private int logged = 0;
	private int toLog = -1;
	
	private ModuleConnector mc = null;
	
	public Logger(int toLog, ModuleConnector mc){
		this.toLog = toLog;
		this.mc = mc;
	}
	
	@Override
	public void onStart() {
		System.out.println("Logger started");
	}

	@Override
	protected void onData(Data<String> data) {
		System.out.println("Logging: "+data.getData());
		logged++;
		if(logged >= toLog){
			finish(false);
		}
	}

	@Override
	public void onStop() {
		System.out.println("Logger stopped");
		mc.stopModules();
	}

}
