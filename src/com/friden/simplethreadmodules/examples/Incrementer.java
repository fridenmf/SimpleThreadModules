package com.friden.simplethreadmodules.examples;

import com.friden.simplethreadmodules.Data;
import com.friden.simplethreadmodules.DataModule;

public class Incrementer extends DataModule<Data<Integer>>{
	
	private DataModule<Data<Integer>> nextModule = null;
	
	public Incrementer(DataModule<Data<Integer>> nextModule){
		this.nextModule = nextModule;
	}
	
	@Override
	public void onStart() {
		System.out.println("Incrementer started");
	}

	@Override
	protected void onData(Data<Integer> data) {
		System.out.println("Incrementing: "+data.getData().toString());
		nextModule.pushData(new Data<Integer>(data.getData()+1));
	}

	@Override
	public void onStop() {
		System.out.println("Incrementer stopped");
	}

}
