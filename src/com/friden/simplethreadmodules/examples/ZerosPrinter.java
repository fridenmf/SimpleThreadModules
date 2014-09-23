package com.friden.simplethreadmodules.examples;

import com.friden.simplethreadmodules.core.Consumer;
import com.friden.simplethreadmodules.core.Producer;

public class ZerosPrinter {
	
	public static void main(String[] arg){
		Consumer c = new IntPrinter(true);
		Producer p = new ZeroProducer(c, true);
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
