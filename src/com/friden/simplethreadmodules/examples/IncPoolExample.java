package com.friden.simplethreadmodules.examples;

import com.friden.simplethreadmodules.core.Consumer;
import com.friden.simplethreadmodules.core.Stash;
import com.friden.simplethreadmodules.core.StashConsumer;
import com.friden.simplethreadmodules.core.Pool;

public class IncPoolExample {
	
	public static void main(String[] arg){
		
		StashIncrementer stashIncer = new StashIncrementer(null, null, false);
		Pool<StashIncrementer, Integer, Integer> pool = new Pool<>(stashIncer, 10, true);
		
		for (int i = 0; i < 20; i++) {
			pool.add(1);
		}
		for (int i = 0; i < 20; i++) {
			System.out.println(pool.get());
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
