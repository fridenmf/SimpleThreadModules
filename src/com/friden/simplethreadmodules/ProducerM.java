package com.friden.simplethreadmodules;

public abstract class ProducerM<M> extends Module {
	
	private ConsumerM<M> nextModule = null;
	
	public ProducerM(ConsumerM<M> nextModule, boolean autostart){
		super(autostart);
		this.nextModule = nextModule;
	}
	
	public abstract M produce();

	@Override
	public final void onLoop() {
		nextModule.push(produce());
	}

}
