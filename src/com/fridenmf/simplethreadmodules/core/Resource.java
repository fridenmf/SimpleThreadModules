package com.fridenmf.simplethreadmodules.core;

import java.util.concurrent.Semaphore;

public class Resource<M> extends Consumer<M>{

	private Semaphore mutexSem = null;
	private Semaphore dataSem  = null;

	private M resource = null;

	public Resource(M resource, boolean autostart) {
		super(false);
		this.resource = resource;
		this.mutexSem = new Semaphore(1);
		this.dataSem  = new Semaphore(0);
		if(autostart){
			start();
		}
	}

	@Override
	protected void onData(M resource) {
		mutexSem.acquireUninterruptibly();
		this.resource = resource; 
		mutexSem.release();
		dataSem.release();
	}

	public M get(){
		dataSem.acquireUninterruptibly();
		mutexSem.acquireUninterruptibly();
		M resource = this.resource;
		mutexSem.release();
		return resource;
	}

}