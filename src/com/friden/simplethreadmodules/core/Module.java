package com.friden.simplethreadmodules.core;

/**
 * @author friden
 * 
 * Convenient class for a task that can be started and loops until finish is called
 */
public abstract class Module {
	
	private Thread thread = null;
	
	private boolean isRunning    = false;
	private boolean isRestarting = false;
	
	/** Called when start is called */
	protected void onStart(){};
	
	/** Should not be left empty as this will be called repeatedly **/
	protected abstract void onLoop();
	
	/** Called when stop is called **/
	protected void onStop(){};
	
	public Module(boolean autostart){
		thread = new Thread(new LifeCycleRunnable());
		if(autostart){
			thread.start();
		}
	}
	
	private final class LifeCycleRunnable implements Runnable {
		@Override
		public void run() {
			do{
				isRestarting = false;
				lifeCycle();
			}while(isRestarting);
		}
	}
	
	public final void start(){
		thread.start();
	}
	
	/** Stops this module */
	public final void stop(){
		isRunning = false;
		thread.interrupt();
	}
	
	/** Breaks the life cycle, skipping onStop and starts over again with onStart 
	 * This will return null to every process waiting on pullData if extended by DataModule */
	public final void restart(){
		isRestarting = true;
		isRunning = false;
		thread.interrupt();
	}
	
	public final boolean isRunning(){
		return isRunning;
	}
	
	private final void lifeCycle(){
		onStart();
		
		isRunning = true;
		while(isRunning){
			onLoop();
		}
		
		if(!isRestarting){
			/* No recursive call to avoid building a huge method call hierarchy */
			onStop();
		}
	}
	
}