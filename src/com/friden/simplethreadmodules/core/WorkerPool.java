package com.friden.simplethreadmodules.core;

import java.util.ArrayList;

public class WorkerPool<M, N> implements Machineable<M, N>{
	
	private SecureStash<M> inStash  = null;
	private SecureStash<N> outStash = null;
	
	private ArrayList<Unstasher<M, N>> workers = null;
	
	/**
	 * Takes an Unstasher with an implemented produce function. 
	 * nextModule can be set to null and autostart has to be
	 * set to false. 
	 * @param roleModel
	 * @param workers
	 */
	public WorkerPool(Unstasher<M, N> roleModel, int numWorkers, boolean autostart){
		inStash  = new SecureStash<>(true);
		outStash = new SecureStash<>(true);
		workers  = new ArrayList<Unstasher<M, N>>();
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
	public N make(M in) {
		add(in);
		return get();
	}
	
	@Override
	public void add(M[] in) {
		for (int i = 0; i < in.length; i++) {
			add(in[i]);
		}
	}

	@Override
	public void add(M in) {
		inStash.add(in);
	}

	@Override
	public N get() {
		return outStash.pull();
	}

}