package com.friden.simplethreadmodules;

/** 
 * Adds received data to the provided stash.
 * Specially convenient to put after a producer
 * @author friden
 */
public class Stasher<M> extends Consumer<M> {
	
	private SecureStash<M> stash = null;
	
	public Stasher(SecureStash<M> stash, boolean autostart){
		super(autostart);
		this.stash = stash;
	}

	@Override
	protected void onData(M data) {
		stash.add(data);
	}

}
