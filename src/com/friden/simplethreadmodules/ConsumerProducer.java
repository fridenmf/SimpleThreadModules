package com.friden.simplethreadmodules;

/** @author friden
 *  @param <N> Type of data to receive
 *  @param <M> Type of data to produce */
public abstract class ConsumerProducer<N, M> extends Consumer<N> {
	
	public abstract M process(N in);
	
	protected Consumer<M> nextModule = null;
	
	/** @param nextModule module to pass produced result to */
	public ConsumerProducer(Consumer<M> nextModule, boolean autostart){
		super(autostart);
		this.nextModule = nextModule;
	}
	
	@Override
	protected final void onData(N data) {
		nextModule.push(process(data));
	}

}
