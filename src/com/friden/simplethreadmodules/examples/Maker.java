package com.friden.simplethreadmodules.examples;

import com.friden.simplethreadmodules.Data;
import com.friden.simplethreadmodules.DataModule;
import com.friden.simplethreadmodules.Module;

/**
 * @author friden
 *
 * Notice that this only exends Module, not DataModule, as it
 * does not listen for data, it only produces it
 */
public class Maker extends Module {
	
	private int made   = 0;
	private int toMake = -1;
	
	private DataModule<Data<String>> nextModule = null;
	
	public Maker(int toMake, DataModule<Data<String>> nextModule){
		this.toMake = toMake;
		this.nextModule = nextModule;
	}

	@Override
	public void onStart() {
		System.out.println("Maker started");
	}
	
	@Override
	public void onLoop() {
		if(made >= toMake){
			finish(false);
		}else{
			made++;
			System.out.println("Making: "+made);
			nextModule.pushData(new Data<String>(Integer.toString(made)));
		}
	}

	@Override
	public void onStop() {
		System.out.println("Maker stopped");
	}
	
}
