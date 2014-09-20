package com.friden.simplethreadmodules.examples;

import com.friden.simplethreadmodules.Data;
import com.friden.simplethreadmodules.DataModule;

public class Shower extends DataModule<Data<Integer>>{
	
	private DataModule<Data<String>> nextModule = null;
	
	public Shower(DataModule<Data<String>> nextModule){
		this.nextModule = nextModule;
	}
	
	@Override
	public void onStart() {
		System.out.println("Shower started");
	}

	@Override
	protected void onData(Data<Integer> data) {
		System.out.println("Showing: "+data.getData().toString());
		nextModule.pushData(new Data<String>(data.getData().toString()));
	}

	@Override
	public void onStop() {
		System.out.println("Shower stopped");
	}

}
