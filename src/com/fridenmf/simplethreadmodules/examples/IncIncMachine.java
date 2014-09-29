package com.fridenmf.simplethreadmodules.examples;

import com.fridenmf.simplethreadmodules.core.Consumer;
import com.fridenmf.simplethreadmodules.core.ConsumerProducer;
import com.fridenmf.simplethreadmodules.core.Machine;
import com.fridenmf.simplethreadmodules.core.MachineFactory;

public class IncIncMachine {
	
	public static void main(String[] arg){
		ConsumerProducer[] workers = new ConsumerProducer[]{
			new Incrementer(null, false),
			new Incrementer(null, false),
			new Incrementer(null, false),
			new Incrementer(null, false),
		};
		
		Machine<Integer, Integer> machine = MachineFactory.assemble(workers);
		
		for (int i = 0; i < workers.length; i++) {
			System.out.println(machine.make(i));
		}
		
		for (int i = 0; i < 100; i++) {
			machine.add(i);
		}
		for (int i = 0; i < 100; i++) {
			System.out.println(machine.get());
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