package com.friden.simplethreadmodules.examples;

import com.friden.simplethreadmodules.core.Consumer;
import com.friden.simplethreadmodules.core.SecureStash;
import com.friden.simplethreadmodules.core.Unstasher;
import com.friden.simplethreadmodules.core.WorkerPool;

public class WorkerPoolExample {
	
	public static void main(String[] arg){
		
		SecureStash<Integer> inStash  = new SecureStash<>(true);
		SecureStash<Integer> outStash = new SecureStash<>(true);
		
		Adder adder = new Adder(inStash, outStash, true);
		
		int items = 1000000;
		long start, end;
		
		/* Here we have one adder, and we let it work on all items */
		
		start = System.currentTimeMillis();
		for (int i = 0; i < items; i++) {
			inStash.add(1);
		}
		for (int i = 0; i < items; i++) {
			outStash.pull();
		}
		end = System.currentTimeMillis();
		System.out.println("Time: "+(end-start)+" ms");
		
		/* Here we instead have many adders in a pool, all working in parallel */
		/* Notice that we only need one extra line to enable this */
		
		WorkerPool<Integer, Integer> pool = new WorkerPool<>(adder, 3, true);
		start = System.currentTimeMillis();
		for (int i = 0; i < items; i++) {
			pool.add(1);
		}
		for (int i = 0; i < items; i++) {
			pool.get();
		}
		end = System.currentTimeMillis();
		System.out.println("Time: "+(end-start)+" ms");
		
		pool.stop();
		adder.stop();
		inStash.stop();
		outStash.stop();
		
	}
	
	private static class Adder extends Unstasher<Integer, Integer> {
		public Adder(SecureStash<Integer> stash, Consumer<Integer> nextModule, boolean autostart) {
			super(stash, nextModule, autostart);
		}

		@Override
		protected Integer produce(Integer data) {
			for (int i = 0; i < 100; i++) {
				data++;
			}
			for (int i = 0; i < 99; i++) {
				data--;
			}
			return data;
		}

		@Override
		public Unstasher<Integer, Integer> copy() {
			return new Adder(stash, nextModule, false);
		}
	}

}
