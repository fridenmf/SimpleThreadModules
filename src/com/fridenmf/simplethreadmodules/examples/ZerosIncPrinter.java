package com.fridenmf.simplethreadmodules.examples;

import com.fridenmf.simplethreadmodules.core.Consumer;
import com.fridenmf.simplethreadmodules.core.ConsumerProducer;
import com.fridenmf.simplethreadmodules.core.Producer;

public class ZerosIncPrinter {
	
	public static void main(String[] arg){
		Consumer c          = new IntPrinter(true);
		ConsumerProducer cp = new Incrementer(c, true); 
		Producer p          = new ZeroProducer(cp, true);
	}
	
	private static class IntPrinter extends Consumer<Integer> {
		public IntPrinter(boolean autostart) {
			super(autostart);
		}

		@Override
		protected void onData(Integer data) {
			System.out.println(data.toString());
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
	
	private static class ZeroProducer extends Producer<Integer> {
		public ZeroProducer(Consumer<Integer> nextModule, boolean autostart) {
			super(nextModule, autostart);
		}

		@Override
		protected Integer produce() {
			return 0;
		}
	}
	
}
