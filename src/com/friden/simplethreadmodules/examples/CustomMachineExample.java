package com.friden.simplethreadmodules.examples;

import com.friden.simplethreadmodules.core.Machineable;
import com.friden.simplethreadmodules.core.SecureStash;
import com.friden.simplethreadmodules.core.Stasher;

import static com.friden.simplethreadmodules.examples.WorkerExamples.*;

public class CustomMachineExample {
	
	public static void main(String[] arg){
		
		IncDecIncToStringMachine iditsm = new IncDecIncToStringMachine();
		
		System.out.println(iditsm.make(1));
		System.out.println(iditsm.make(2));
		System.out.println(iditsm.make(3));
		System.out.println(iditsm.make(4));
	
	}
	
	private static class IncDecIncToStringMachine implements Machineable<Integer, String>{
		
		/* our factory consists of 5 workers */
		
		private IncrementWorker w0 = null; // worker 0 increments
		private DecrementWorker w1 = null; // worker 1 decrements
		private IncrementWorker w2 = null; // worker 2 increments
		private ToStringWorker  w3 = null; // worker 3 makes strings from ints
		
		/* This is our last worker, it takes the result from the last producer 
		 * and places it in storage, ready for the program to take */
		private Stasher<String> storer = null;
		
		/* This is our storage, its thread safe, the program takes results from here */
		private SecureStash<String> results = null;
		
		public IncDecIncToStringMachine(){
			results = new SecureStash<>(true);
			storer = new Stasher<String>(results, true);
			w3 = new ToStringWorker(storer, true);
			w2 = new IncrementWorker(w3, 1, true);
			w1 = new DecrementWorker(w2, 1, true);
			w0 = new IncrementWorker(w1, 1, true);
		}
		
		@Override
		public String make(Integer in){
			w0.add(in);
			return results.pull();
		}
		
		@Override
		public void add(Integer[] in) {
			for (int i = 0; i < in.length; i++) {
				add(in[i]);
			}
		}

		@Override
		public void add(Integer in) {
			w0.add(in);
		}

		@Override
		public String get() {
			return results.pull();
		}
		
	}
	
}