package com.friden.simplethreadmodules.examples;

import com.friden.simplethreadmodules.core.Consumer;
import com.friden.simplethreadmodules.core.ConsumerProducer;
import com.friden.simplethreadmodules.core.Stash;

public class IncIncStash {
	
	public static void main(String[] arg){
		Stash<Integer> result = new Stash<>(true);
		Incrementer middle    = new Incrementer(result, true); 
		Incrementer start     = new Incrementer(middle, true);
		
		start.add(0);
		System.out.println(result.get());
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
