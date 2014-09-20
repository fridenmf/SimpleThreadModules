package com.friden.simplethreadmodules;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

public class SecureStash<M> {
	
	private Semaphore mutexSem = null;
	private Semaphore dataSem  = null;
	
	private Queue<M> resources = null;
	
	public SecureStash(){
		resources = new LinkedList<M>();
		mutexSem  = new Semaphore(1);
		dataSem   = new Semaphore(0);
	}
	
	public void push(M resource){
		mutexSem.acquireUninterruptibly();
		resources.add(resource);
		mutexSem.release();
		dataSem.release();
	}
	
	public M pull(){
		dataSem.acquireUninterruptibly();
		mutexSem.acquireUninterruptibly();
		M resource = resources.poll();
		mutexSem.release();
		return resource;
	}

}