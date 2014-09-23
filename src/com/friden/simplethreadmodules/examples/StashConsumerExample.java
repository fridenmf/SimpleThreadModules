package com.friden.simplethreadmodules.examples;

import com.friden.simplethreadmodules.core.Consumer;
import com.friden.simplethreadmodules.core.Stash;
import com.friden.simplethreadmodules.core.StashConsumer;

public class StashConsumerExample {
	
	public static void main(String[] arg){
		Stash<Integer> in  = new Stash<>(true);
		Stash<Integer> out = new Stash<>(true);
		
		StashIncrementer si0 = new StashIncrementer(in, out, true);
		StashIncrementer si1 = new StashIncrementer(in, out, true);
		StashIncrementer si2 = new StashIncrementer(in, out, true);
		
		for (int i = 0; i < 20; i++) {
			in.add(i);
		}
		for (int i = 0; i < 20; i++) {
			System.out.println(out.get());
		}
	}
	
	private static class StashIncrementer extends StashConsumer<StashIncrementer, Integer, Integer> {
		
		public StashIncrementer(Stash<Integer> stash, Consumer<Integer> nextModule, boolean autostart) {
			super(stash, nextModule);
			if(autostart){
				start();
			}
		}

		@Override
		protected Integer produce(Integer data) {
			return data + 1;
		}

		@Override
		public StashIncrementer copy() {
			return new StashIncrementer(stash, nextModule, false);
		}
	}
}
