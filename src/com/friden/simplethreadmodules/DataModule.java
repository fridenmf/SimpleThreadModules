package com.friden.simplethreadmodules;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

/**
 * @author friden
 *
 * Class listening for packages of type M
 * Convenient for tasks that will run in parallel
 * Its sleeping when the data queue is empty
 */
public abstract class DataModule<M extends Data<?>> extends Module {
	
	private Queue<M> queue = null;
	
	private Semaphore mutexSem = null;
	private Semaphore dataSem  = null;
	
	protected abstract void onData(M textMessage);
	
	public DataModule(){
		queue = new LinkedList<M>();
		mutexSem = new Semaphore(1);
		dataSem = new Semaphore(0);
	}
	
	public final void onLoop(){
		M data = pullData();
		if(data != null){
			onData(data);
		}
	}
	
	private M pullData(){
		boolean successfulAcquire = false;
		while(!successfulAcquire){
			try {
				dataSem.acquire();
				successfulAcquire = true;
			} catch (InterruptedException e) {
				if(!isRunning){
					return null;
				}
			}
		}
		
		mutexSem.acquireUninterruptibly();
		M data = queue.poll();
		mutexSem.release();
		
		return data;
	}
	
	public void pushData(M data){
		mutexSem.acquireUninterruptibly();
		queue.add(data);
		mutexSem.release();
		
		dataSem.release();
	}

}