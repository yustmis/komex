package com.list.lod.test;


import java.io.File;
import java.util.Iterator;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.list.lod.collector.Factory;
import com.list.lod.collector.FetchFactory;
import com.list.lod.collector.InsModule;
import com.list.lod.collector.Type;
import com.list.lod.manager.BuildManager;
import com.list.lod.util.FileUtil;
import com.list.lod.util.ResourceBundle;


/**
 * FetchTest 
 * @author thsohn
 *
 */
public class FetchTest {
	
	InsModule module = null;
	long startTime = 0l;
	int fetch = 0;
	
	@Before
	public void testInst() throws Exception {
		module = Factory.createModule(Type.FILE);
		String size = ResourceBundle.fileBundle.getString("fetch_size");
		fetch = Integer.parseInt(size);
	}
	
	/**
	 * 패치 할 때에 하나 하나 돌리는 데, Fetch Size 만큼 돌리고 파일에 append 시켜주고, 그 다음 또 다시 Fetch Size 만큼 돌리고 파일에 append 시켜주고 ... 이렇고 저렇고 ... 
	 * @throws Exception
	 */
//	@Ignore
	@Test
	public void testMain() throws Exception {
		System.out.println("Started ..."); // 정상으로 나옴을 확인할 수 있음.
		startTime = System.currentTimeMillis();
		Iterator<String> iterator = InsModule.insMap.keySet().iterator();
		
		FetchFactory factory = new FetchFactory();
		int i = 1;
		while(iterator.hasNext()){
			
			String uri = iterator.next();
			factory.fetch(uri, InsModule.insMap.get(uri));
			
			if(i == fetch){
				factory.setFetchFlag(true);
				factory.writeResultToFile(); // fetch size 랑 맞을 경우 불러들일 때마다 파일에 어팬드 시킨다.
				i = 1;
				continue;
			}
			
			i++;
		}
		factory.writeResultToFile(); // 제일 마지막 file Write 
		long total = System.currentTimeMillis() - startTime;
		
		
		//먼저 온토베이스 데이터 폴더에 파일을 복사한다.  
		// ********* add build Build Manager ********
		String filename = factory.getOutputFile(); //생성한 파일 이름을 가져와서 .... 
		
		//경로를 가져온 다음에 파일 카피
		FileUtil.fileCopy(filename);
		BuildManager manager = new BuildManager();
		manager.doAddBuild(filename);
		
		System.out.println("Finished ... " + total/1000.0+"s");
	}
	
	@Ignore
	@Test
	public void testMain1() throws Exception {
		System.out.println("Started ..."); // 정상으로 나옴을 확인할 수 있음.
		Iterator<String> iterator = InsModule.insMap.keySet().iterator();
		
		FetchFactory factory = new FetchFactory();
		while(iterator.hasNext()){
			String uri = iterator.next();
			factory.fetch(uri, InsModule.insMap.get(uri));
		}
		//factory.writeResultToFile();
		long total = System.currentTimeMillis() - startTime;
		System.out.println("Finished ... " + total/1000.0+"s");
	}
}
