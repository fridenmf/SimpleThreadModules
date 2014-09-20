package com.friden.simplethreadmodules;

/**
 * @author friden
 * 
 * Convenient class for a task that can be started and loops until finish is called
 *
 */
public abstract class Module extends Thread {
	
	public boolean isRunning = false;
	
	public abstract void onStart();
	
	/** Should not be left empty as this will be called repeatedly **/
	public abstract void onLoop();
	
	public abstract void onStop();
	
	@Override
	public void run() {
		super.run();
		
		onStart();
		
		isRunning = true;
		while(isRunning){
			onLoop();
		}
		
		onStop();
		
	}
	
	public void finish(boolean interrupt){
		isRunning = false;
		if(interrupt){
			this.interrupt();
		}
	}
	
	public boolean isFinished(){
		return !isRunning;
	}
	
}