package com.list.lod.util;

import java.util.StringTokenizer;

public class StringUtil {
	public static String getDomainIdentation(String uri){
		if(uri != null){
			boolean found = false;
			String name = "";
			StringTokenizer tokenizer  = new StringTokenizer(uri, ".");
			int i = 0;
			while(tokenizer.hasMoreTokens()){
				String nextToken = tokenizer.nextToken();
				
				if(found){
					name = nextToken;
					break;
				}
				
				// www이 들어가지 않은 domain address 
				if(nextToken.contains("lod")){
					found = true;
				}else if(i==0){ // 도메인의 두번째 토큰일 경우 ... 
					found = true;
				}else {
					System.out.println("Not Find");
				}
				i++;
			}
			return name;
		}else{
			throw new NullPointerException("Input URI is null !");
		}
	}
}
