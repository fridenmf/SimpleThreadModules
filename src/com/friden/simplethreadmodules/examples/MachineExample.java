package com.friden.simplethreadmodules.examples;

import com.friden.simplethreadmodules.ConsumerProducer;
import com.friden.simplethreadmodules.MachineFactory;
import com.friden.simplethreadmodules.Machine;

import static com.friden.simplethreadmodules.examples.WorkerExamples.*;

public class MachineExample {
	
	public static void main(String[] arg){
		
		@SuppressWarnings("rawtypes")
		ConsumerProducer[] workers = new ConsumerProducer[]{
			new IncrementWorker(null, 1, false),
			new IncrementWorker(null, 1, false),
			new IncrementWorker(null, 1, false),
			new IncrementWorker(null, 1, false),
			new IncrementWorker(null, 1, false),
			new IncrementWorker(null, 1, false),
			new IncrementWorker(null, 1, false),
			new IncrementWorker(null, 1, false),
			new IncrementWorker(null, 1, false),
			new IncrementWorker(null, 1, false),
		};
		
		Machine<Integer, Integer> machine = MachineFactory.assemble(workers);
		
		/* Now we have our machine that does inc 10 times */
		
		/* Our machine has two methods of producing results: 
		 * make - puts something in and waits for the result
		 * add  - puts something on the line, dont wait for result 
		 * get  - waits for result from the machine 
		 * 
		 * When using add get, it's possible to queue multiple inputs
		 * and thereby using its full potential 
		 * 
		 * Here follows a comparison between the two approaches */
		
		int inputs = 100000;
		long start, end;
		
		/* Add followed by Get */
		start = System.currentTimeMillis();
		for (int i = 0; i < inputs; i++) {
			machine.add(i);
		}
		for (int i = 0; i < inputs; i++) {
			machine.get();
		}
		end = System.currentTimeMillis();
		System.out.println("Time(add + get): "+(end-start)+" ms");
		
		/* Make */
		start = System.currentTimeMillis();
		for (int i = 0; i < inputs; i++) {
			machine.make(i);
		}
		end = System.currentTimeMillis();
		System.out.println("Time(make): "+(end-start)+" ms");
		
	}
	
}