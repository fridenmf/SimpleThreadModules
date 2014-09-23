package com.friden.simplethreadmodules.core;

public abstract class Producer<M> extends Module {
	
	protected Consumer<M> nextModule = null;
	
	public Producer(Consumer<M> nextModule, boolean autostart){
		super(false);
		this.nextModule = nextModule;
		if(autostart){
			start();
		}
	}
	
	protected abstract M produce();

	@Override
	protected final void onLoop() {
		nextModule.add(produce());
	}

}
