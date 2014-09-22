package com.friden.simplethreadmodules.examples;

import com.friden.simplethreadmodules.core.Consumer;
import com.friden.simplethreadmodules.core.ConsumerProducer;
import com.friden.simplethreadmodules.core.Producer;

public class WorkerExamples {
	
	/** This worker converts integers to strings */
	public static class ToStringWorker extends ConsumerProducer<Integer, String>{
		public ToStringWorker(Consumer<String> nextModule, boolean autostart) {
			super(nextModule, autostart);
		}
		@Override
		public String process(Integer in) {
			return in.toString();
		}
	}
	
	/** This worker increments integers */
	public static class IncrementWorker extends ConsumerProducer<Integer, Integer>{
		private Integer di;
		public IncrementWorker(Consumer<Integer> nextModule, Integer di, boolean autostart) {
			super(nextModule, autostart);
			this.di = di;
		}
		@Override
		public Integer process(Integer in) {
			return in + di;
		}
	}
	
	/** This worker decrements integers */
	public static class DecrementWorker extends ConsumerProducer<Integer, Integer>{
		private Integer di;
		public DecrementWorker(Consumer<Integer> nextModule, Integer di, boolean autostart) {
			super(nextModule, autostart);
			this.di = di;
		}
		@Override
		public Integer process(Integer in) {
			return in - di;
		}
	}
	
	/** This worker logs strings to the terminal */
	public static class Logger extends Consumer<String>{
		public Logger(boolean autostart){
			super(autostart);
		}
		@Override
		protected void onData(String data) {
			System.out.println(data);
		}
	}
	
	/** This worker produces (to-from) integers */
	public static class IntegerProducer extends Producer<Integer>{
		private int current = 0;
		private int to;
		public IntegerProducer(Consumer<Integer> nextModule, int from, int to, boolean autostart) {
			super(nextModule, autostart);
			this.current = from;
			this.to = to;
		}
		@Override
		public Integer produce() {
			if(current++ > to){
				stop();
			}
			return current;
		}
	}

}
