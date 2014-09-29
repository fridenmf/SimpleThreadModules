package com.fridenmf.simplethreadmodules.core;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author friden
 * This class makes a bit magic, use it with caution
 */
public class MachineFactory {
	
	/**
	 * @param takes an array of modules that has not yet started. These
	 * can be created by new ConsumerProducerM<M,N>(null, false); 
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <M, N> Machine<M, N> assemble(ConsumerProducer[] modules){
		
		for (int i = 1; i < modules.length; i++) {
			if(modules[i-1].isRunning()){
				return null;
			}
			if(getGenericTypes(modules[i-1])[1] == getGenericTypes(modules[i-0])[0]){
				modules[i-1].nextModule = modules[i-0];
			}else{
				return null;
			}
		}
		
		Machine<M,N> machine = new Machine<M, N>(modules[0], modules[modules.length-1]);
				
		for (int i = 0; i < modules.length; i++) {
			modules[i].start();
		}
		
		return machine;
	}
	
	public static Type[] getGenericTypes(Object o){
		return ((ParameterizedType) o.getClass().getGenericSuperclass()).getActualTypeArguments();
	}

}
