package com.friden.simplethreadmodules.core;

public abstract class StashConsumer<M,N,O> extends Producer<O> implements Copyable<M> {

	protected Stash<N> stash = null;
	
	protected abstract O produce(N data);
	
	public StashConsumer(StashConsumer<M,N,O> roleModel, boolean autostart){
		super(roleModel.nextModule, autostart);
		this.stash = roleModel.stash;
	}
	
	public StashConsumer(Stash<N> stash, Consumer<O> nextModule) {
		super(nextModule, false);
		this.stash = stash;
	}

	@Override
	protected final O produce() {
		return produce(stash.get());
	}
	
	protected StashConsumer<M,N,O> reconstruct(Stash<N> stash, Consumer<O> nextModule, boolean autostart){
		this.stash = stash;
		this.nextModule = nextModule;
		if(autostart){ 
			start(); 
		}
		return this;
	}

}
