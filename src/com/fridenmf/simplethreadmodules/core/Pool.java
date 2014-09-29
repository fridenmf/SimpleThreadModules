package com.fridenmf.simplethreadmodules.core;

import java.util.ArrayList;

public class Pool<M extends StashConsumer<M,N,O>, N, O> implements Machineable<N,O>{
	
	private Stash<N> inStash  = null;
	private Stash<O> outStash = null;
	
	private ArrayList<StashConsumer<M,N,O>> workers = null;
	
	/**
	 * Takes an Unstasher with an implemented produce function. 
	 * nextModule can be set to null and autostart has to be
	 * set to false. 
	 * @param roleModel
	 * @param workers
	 */
	public Pool(StashConsumer<M,N,O> roleModel, int numWorkers, boolean autostart){
		inStash  = new Stash<>(true);
		outStash = new Stash<>(true);
		workers  = new ArrayList<StashConsumer<M,N,O>>();
		for (int i = 0; i < numWorkers; i++) {
			workers.add(roleModel.copy().reconstruct(inStash, outStash, false));
		}
		start();
	}
	
	public void start(){
		for (int i = 0; i < workers.size(); i++) {
			workers.get(i).start();
		}
	}
	
	public void stop(){
		for (int i = 0; i < workers.size(); i++) {
			workers.get(i).stop();
		}
		inStash.stop();
		outStash.stop();
	}
	
	@Override
	public O make(N in) {
		add(in);
		return get();
	}
	
	@Override
	public void add(N in) {
		inStash.add(in);
	}

	@Override
	public O get() {
		return outStash.get();
	}

}