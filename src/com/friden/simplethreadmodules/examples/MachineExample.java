package com.friden.simplethreadmodules.examples;

import com.friden.simplethreadmodules.ConsumerM;
import com.friden.simplethreadmodules.ConsumerProducerM;
import com.friden.simplethreadmodules.MachineM;
import com.friden.simplethreadmodules.ProducerM;
import com.friden.simplethreadmodules.SecureResource;
import com.friden.simplethreadmodules.Storer;

public class MachineExample {
	
	public static void main(String[] arg){
		
		IncDecIncToStringMachine iditsm = new IncDecIncToStringMachine();
	
		System.out.println(iditsm.make(0));
		
	}
	
	private static class IncDecIncToStringMachine implements MachineM<Integer, String> {
		
		/* our factory consists of 5 workers */
		
		private IncrementWorker w0 = null; // worker 0 increments
		private DecrementWorker w1 = null; // worker 1 decrements
		private IncrementWorker w2 = null; // worker 2 increments
		private ToStringWorker  w3 = null; // worker 3 makes strings from ints
		
		/* This is our last worker, it takes the result from the last producer 
		 * and places it in storage, ready for the program to take */
		private Storer<String> storer = null;
		
		/* This is our storage, its thread safe, the program takes results from here */
		private SecureResource<String> results = null;
		
		public IncDecIncToStringMachine(){
			results = new SecureResource<>();
			storer = new Storer<String>(results, true);
			w3 = new ToStringWorker(storer, true);
			w2 = new IncrementWorker(w3, true);
			w1 = new DecrementWorker(w2, true);
			w0 = new IncrementWorker(w1, true);
		}
		
		@Override
		public String make(Integer in){
			w0.push(in);
			return results.pull();
		}
		
	}
	
	/** This worker converts integers to strings */
	private static class ToStringWorker extends ConsumerProducerM<Integer, String>{
		public ToStringWorker(ConsumerM<String> nextModule, boolean autostart) {
			super(nextModule, autostart);
		}
		@Override
		public String process(Integer in) {
			return in.toString();
		}
	}
	
	/** This worker increments integers */
	private static class IncrementWorker extends ConsumerProducerM<Integer, Integer>{
		public IncrementWorker(ConsumerM<Integer> nextModule, boolean autostart) {
			super(nextModule, autostart);
		}
		@Override
		public Integer process(Integer in) {
			return in + 1;
		}
	}
	
	/** This worker decrements integers */
	private static class DecrementWorker extends ConsumerProducerM<Integer, Integer>{
		public DecrementWorker(ConsumerM<Integer> nextModule, boolean autostart) {
			super(nextModule, autostart);
		}
		@Override
		public Integer process(Integer in) {
			return in - 1;
		}
	}
	
	/** Not used, just here as an example */
	/** This worker logs strings to the terminal */
	@SuppressWarnings("unused")
	private static class Logger extends ConsumerM<String>{
		public Logger(boolean autostart){
			super(autostart);
		}
		@Override
		protected void onData(String data) {
			System.out.println(data);
		}
	}
	
	/** Not used, just here as an example */
	/** This worker produces (to-from) integers */
	@SuppressWarnings("unused")
	private static class IntegerProducer extends ProducerM<Integer>{
		private int current = 0;
		private int to;
		public IntegerProducer(ConsumerM<Integer> nextModule, int from, int to, boolean autostart) {
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