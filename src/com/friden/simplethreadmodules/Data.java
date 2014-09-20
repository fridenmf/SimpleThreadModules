package com.friden.simplethreadmodules;

/**
 * @author friden
 *
 * Container for data of type T
 */
public class Data<T> {
	
	private T data = null;
	
	public Data(T data){ 
		this.data = data; 
	}
	
	public T getData(){ 
		return this.data; 
	}
	
}
