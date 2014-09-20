package com.friden.simplethreadmodules;

public interface Machineable<M, N> {
	
	/** Feeds the machine and waits for the results, returns it when it arrives */
	public N make(M in);

	/** Feeds the machine but lefts the result in the machine */
	public void add(M in);
	
	/** Waits for a result to arrive, and takes it when it does */
	public N get();

}
