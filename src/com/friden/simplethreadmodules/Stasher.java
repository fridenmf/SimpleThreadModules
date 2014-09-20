package com.friden.simplethreadmodules;

public class Stasher<M> extends Consumer<M> {
	
	private SecureStash<M> stash = null;
	
	public Stasher(SecureStash<M> stash, boolean autostart){
		super(autostart);
		this.stash = stash;
	}

	@Override
	protected void onData(M data) {
		stash.push(data);
	}

}
