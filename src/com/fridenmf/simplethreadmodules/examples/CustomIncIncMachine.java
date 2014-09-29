package com.fridenmf.simplethreadmodules.examples;

import com.fridenmf.simplethreadmodules.core.Consumer;
import com.fridenmf.simplethreadmodules.core.ConsumerProducer;
import com.fridenmf.simplethreadmodules.core.Machineable;
import com.fridenmf.simplethreadmodules.core.Stash;

public class CustomIncIncMachine {
	
	public static void main(String[] arg){
		
		IncIncMachine machine = new IncIncMachine();
		
		/* Put one thing into the machine, wait for out put, repeat */		
		for (int i = 0; i < 10; i++) {
			System.out.println(machine.make(i));
		}
		
		/* Put ten things into the machine, collect all results */		
		for (int i = 0; i < 10; i++) {
			machine.add(i);
		}
		for (int i = 0; i < 10; i++) {
			System.out.println(machine.get());
		}
	
	}
	
	private static class IncIncMachine implements Machineable<Integer, Integer>{
		
		/* Our machine consists of 2 workers */
		private Incrementer i0 = null;
		private Incrementer i1 = null;
		
		/* This is our storage, its thread safe, the program takes results from here */
		private Stash<Integer> results = null;
		
		public IncIncMachine(){
			results = new Stash<>(true);
			i1 = new Incrementer(results, true);
			i0 = new Incrementer(i1, true);
		}
		
		@Override
		public Integer make(Integer in){
			i0.add(in);
			return results.get();
		}
		
		@Override
		public void add(Integer in) {
			i0.add(in);
		}

		@Override
		public Integer get() {
			return results.get();
		}
	}
	
	private static class Incrementer extends ConsumerProducer<Integer, Integer> {
		public Incrementer(Consumer<Integer> nextModule, boolean autostart) {
			super(nextModule, autostart);
		}

		@Override
		public Integer process(Integer in) {
			return in + 1;
		}
	}
	
}