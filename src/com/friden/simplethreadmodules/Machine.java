package com.friden.simplethreadmodules;

public class Machine<M, N> {
	
	private ConsumerProducer<M, ?> first = null;
	
	private SecureStash<N> result = null;
	private Stasher<N> storer = null; 
	
	/** Takes the first and last of connected workers, and puts a storer at the end */
	protected Machine(ConsumerProducer<M, ?> first, ConsumerProducer<?, N> last){
		this.first = first;
		this.result = new SecureStash<N>();
		this.storer = new Stasher<N>(result, true);
		last.nextModule = storer;
	}
	
	/** Feeds the machine and waits for the results, returns it when it arrives */
	public N make(M in){
		first.push(in);
		return result.pull();
	}

	/** Feeds the machine but lefts the result in the machine */
	public void add(M in){
		first.push(in);
	}
	
	/** Waits for a result to arrive, and takes it when it does */
	public N get(){
		return result.pull();
	}
	
}
