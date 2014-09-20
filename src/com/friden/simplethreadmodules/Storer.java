package com.friden.simplethreadmodules;

public class Storer<M> extends ConsumerM<M> {
	
	private SecureResource<M> stash = null;
	
	public Storer(SecureResource<M> stash, boolean autostart){
		super(autostart);
		this.stash = stash;
	}

	@Override
	protected void onData(M data) {
		stash.push(data);
	}

}
