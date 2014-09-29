package com.fridenmf.simplethreadmodules.core;

public class Machine<M, N> implements Machineable<M, N>{
	
	private ConsumerProducer<M, ?> first = null;
	private Stash<N> result = null;
	
	/** Takes the first and last of connected workers, and puts a storer at the end */
	protected Machine(ConsumerProducer<M, ?> first, ConsumerProducer<?, N> last){
		this.first = first;
		this.result = new Stash<N>(true);
		last.nextModule = result;
	}
	
	@Override
	public N make(M in){
		first.add(in);
		return result.get();
	}
	
	@Override
	public void add(M in){
		first.add(in);
	}
	
	@Override
	public N get(){
		return result.get();
	}
	
}
