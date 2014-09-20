package com.friden.simplethreadmodules;

import java.util.ArrayList;
import java.util.List;

/**
 * @author friden
 *
 *	Class for connecting (or just managing) modules.
 *	Provides methods for managing a list of modules.
 */
public class ModuleConnector {
	
	private ArrayList<Module> modules = null;
	
	public ModuleConnector(){
		modules = new ArrayList<>();
	}
	
	public void addModule(Module module){
		modules.add(module);
	}
	
	public void removeModule(int index){
		modules.remove(index);
	}
	
	public void removeModule(Module module){
		modules.remove(module);
	}
	
	public void startModule(int index){
		modules.get(index).start();
	}
	
	public int size(){
		return modules.size();
	}
	
	/**
	 * It is possible to start a module by just calling start on the 
	 * module, but this method is provided anyway for convenience
	 */
	public void startModule(Module module){
		module.start();
	}
	
	public void startModules(){
		for (int i = 0; i < modules.size(); i++) {
			modules.get(i).start();
		}
	}
	
	public void stopModules(){
		for (int i = 0; i < modules.size(); i++) {
			modules.get(i).stop();
		}
	}

	public void addModules(List<Module> modules) {
		modules.addAll(modules);
	}

}
