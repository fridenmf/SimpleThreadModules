package com.friden.simplethreadmodules;

public abstract class Unstasher<M, N> extends Producer<N> implements Copyable<Unstasher<M, N>> {

	protected SecureStash<M> stash = null;
	
	protected abstract N produce(M data);
	
	public Unstasher(Unstasher<M, N> roleModel, boolean autostart){
		super(roleModel.nextModule, autostart);
		this.stash = roleModel.stash;
	}
	
	public Unstasher(SecureStash<M> stash, Consumer<N> nextModule, boolean autostart) {
		super(nextModule, autostart);
		this.stash = stash;
	}

	@Override
	protected final N produce() {
		return produce(stash.pull());
	}
	
	protected Unstasher<M, N> reconstruct(SecureStash<M> stash, Consumer<N> nextModule, boolean autostart){
		this.stash = stash;
		this.nextModule = nextModule;
		if(autostart){ 
			start(); 
		}
		return this;
	}

}
