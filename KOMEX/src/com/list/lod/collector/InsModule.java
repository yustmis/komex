package com.list.lod.collector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface InsModule {
	public HashMap<String, InfoWorker> insMap = new HashMap<String, InfoWorker>(); 
	public static List<String> domainList = new ArrayList<String>();
	public InsModule create();
	void init();
}
