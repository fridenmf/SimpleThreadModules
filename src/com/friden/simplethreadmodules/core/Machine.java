package com.friden.simplethreadmodules.core;

public class Machine<M, N> implements Machineable<M, N>{
	
	private ConsumerProducer<M, ?> first = null;
	
	private SecureStash<N> result = null;
	private Stasher<N> storer = null; 
	
	/** Takes the first and last of connected workers, and puts a storer at the end */
	protected Machine(ConsumerProducer<M, ?> first, ConsumerProducer<?, N> last){
		this.first = first;
		this.result = new SecureStash<N>(true);
		this.storer = new Stasher<N>(result, true);
		last.nextModule = storer;
	}
	
	@Override
	public N make(M in){
		first.add(in);
		return result.pull();
	}
	
	@Override
	public void add(M[] in) {
		for (int i = 0; i < in.length; i++) {
			add(in[i]);
		}
	}

	@Override
	public void add(M in){
		first.add(in);
	}
	
	@Override
	public N get(){
		return result.pull();
	}
	
}
