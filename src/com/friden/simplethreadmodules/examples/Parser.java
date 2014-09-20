package com.friden.simplethreadmodules.examples;

import com.friden.simplethreadmodules.Data;
import com.friden.simplethreadmodules.DataModule;

public class Parser extends DataModule<Data<String>>{
	
	private DataModule<Data<Integer>> nextModule = null;
	
	public Parser(DataModule<Data<Integer>> nextModule){
		this.nextModule = nextModule;
	} 
	
	@Override
	public void onStart() {
		System.out.println("Parser started");
	}

	@Override
	protected void onData(Data<String> data) {
		System.out.println("Parsing: "+data.getData());
		nextModule.pushData(new Data<Integer>(Integer.parseInt(data.getData())));
	}

	@Override
	public void onStop() {
		System.out.println("Parser stopped");
	}

}
