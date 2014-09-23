package com.friden.simplethreadmodules.core;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

public class Stash<M> extends Consumer<M>{

	private Semaphore mutexSem = null;
	private Semaphore dataSem  = null;

	private Queue<M> resources = null;

	public Stash(boolean autostart) {
		super(false);
		resources = new LinkedList<M>();
		mutexSem  = new Semaphore(1);
		dataSem   = new Semaphore(0);
		if(autostart){
			start();
		}
	}

	@Override
	protected void onData(M data) {
		mutexSem.acquireUninterruptibly();
		resources.add(data);
		mutexSem.release();
		dataSem.release();
	}

	public M get(){
		dataSem.acquireUninterruptibly();
		mutexSem.acquireUninterruptibly();
		M resource = resources.poll();
		mutexSem.release();
		return resource;
	}

}