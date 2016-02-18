package com.list.lod.collector;

public class IlligalCallExection extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public IlligalCallExection() {
		super("You should have call createModule method first!!");
	}
}
