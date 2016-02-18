package com.list.lod.collector;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import com.list.lod.util.ResourceBundle;
import com.list.lod.util.StringUtil;

public class FileInsModule implements InsModule {

	private FileInsModule instance;

	public FileInsModule() {
		String filePath = ResourceBundle.fileBundle.getString("instance_file_path");
		readFile(filePath);
		init();
	}
	
	@Override
	public FileInsModule create() {
		if (instance == null)
			instance = new FileInsModule();
		return instance;
	}

	@Override
	public void init() {
		String outputList = ResourceBundle.fileBundle
				.getString("output_list");
		getpreSpecifiedDomain(outputList);
	}

	private void readFile(String filePath) {
		BufferedReader br = null;
		String sCurrentLine = null;
		try {
			br = new BufferedReader(new FileReader(filePath));
			while ((sCurrentLine = br.readLine()) != null) {
				String uri = sCurrentLine;
				String domainName = StringUtil.getDomainIdentation(uri.trim());
//				System.out.println("URI ::: " + uri + " Domain Name :: " + domainName);
				if (ResourceBundle.bundle.containsKey(domainName)) {
					String prop = ResourceBundle.bundle.getString(domainName);
					InfoWorker worker = compressFetchProp(prop, domainName);
					insMap.put(uri, worker); //
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	private InfoWorker compressFetchProp(String fetchProp,String domainName) { 
		if (fetchProp != null) {
			InfoWorker worker = null;
			StringTokenizer tokenizer = new StringTokenizer(fetchProp, ",");
			List<String> list = new ArrayList<String>();
			int i = 0;
			String type = "";
			while (tokenizer.hasMoreTokens()) {
				if (i == 0) {
					type = tokenizer.nextToken(); // return type(output)
				} else {
					String nextToken = tokenizer.nextToken();
					list.add(nextToken);
				}
				i++;
			}
			
			worker = (!"".equals(domainName) && !"".equals(type) && !list
					.isEmpty()) ? new InfoWorker(type, domainName, list)
					: worker;
			return worker;
		} else {
			throw new NullPointerException("Input fetchProp is Null ");
		}
	}
	
	private void getpreSpecifiedDomain(String domainString){
		if(domainString != null){
			StringTokenizer tokenizer = new StringTokenizer(domainString, ",");
			while(tokenizer.hasMoreTokens()){
					String nextToken = tokenizer.nextToken();
					domainList.add(nextToken);
			}
		}else{
			throw new NullPointerException("Input fetchProp is Null ");
		}
	}

}
