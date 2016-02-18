package com.list.lod.collector;

public class Factory {
	
	private static InsModule module;
	
	public static InsModule createModule(Type type){
		switch (type) {
		case FILE:
			module = new FileInsModule();
			break;
		case ONTO:
			break;
			
		default:
			
			break;
		}
		return module;
	}
	
	public static InsModule getModule() throws IlligalCallExection{
		if(module==null)	
			throw new IlligalCallExection();
		return module;
	};
}
