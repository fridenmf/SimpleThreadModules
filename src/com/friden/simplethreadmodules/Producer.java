package com.friden.simplethreadmodules;

public abstract class Producer<M> extends Module {
	
	protected Consumer<M> nextModule = null;
	
	public Producer(Consumer<M> nextModule, boolean autostart){
		super(autostart);
		this.nextModule = nextModule;
	}
	
	protected abstract M produce();

	@Override
	protected final void onLoop() {
		nextModule.add(produce());
	}

}
