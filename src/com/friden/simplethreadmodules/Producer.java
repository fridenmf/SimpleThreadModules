package com.friden.simplethreadmodules;

public abstract class Producer<M> extends Module {
	
	private Consumer<M> nextModule = null;
	
	public Producer(Consumer<M> nextModule, boolean autostart){
		super(autostart);
		this.nextModule = nextModule;
	}
	
	public abstract M produce();

	@Override
	public final void onLoop() {
		nextModule.push(produce());
	}

}
