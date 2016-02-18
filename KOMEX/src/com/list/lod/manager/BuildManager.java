package com.list.lod.manager;

import java.io.File;
import java.io.FileNotFoundException;

import com.list.facility.publish.ExcuteShell;
import com.list.facility.publish.OntoBaseManager;
import com.list.index.IndexForFile;
import com.list.lod.util.ResourceBundle;
import com.list.util.PropInitialize;

/**
 * Ontobase Build Manager 
 * @author thsohn
 *
 */
public class BuildManager {
	public void doAddBuild(String fileName) throws FileNotFoundException {
		// file add build
		String textsearch = ResourceBundle.fileBundle.getString("textsearch.path");
		String ip = ResourceBundle.fileBundle.getString("ip");
		String port = ResourceBundle.fileBundle.getString("port");
		String serviceName = ResourceBundle.fileBundle.getString("serviceName");
		String storeName = ResourceBundle.fileBundle.getString("storeName");
		String dataDir = ResourceBundle.fileBundle.getString("ontobase_dir");
		
		OntoBaseManager obm = new OntoBaseManager();
		String absolutePath = dataDir + File.separator + fileName+".ttl"; 
		File file = new File(absolutePath);
		if(file.exists() && !file.isDirectory()){
			//Add Build
			System.out.println("[Delete Build] Start ... ");
			obm.deleteBuild(ip, Integer.parseInt(port), serviceName, storeName, file.getAbsolutePath().toString());
			System.out.println("[Delete Build] Finish ... ");
			System.out.println("[Add Build] Start ... ");
			obm.addBuild(ip, Integer.parseInt(port), serviceName, storeName, file.getAbsolutePath().toString());
			System.out.println("[Add Build] Finish ... ");
			ExcuteShell exs = new ExcuteShell();
			// TextSearch 인덱스
			System.setProperty(PropInitialize.KEY_TEXTSEARCH_HOME_DIR, textsearch);
			PropInitialize.getInstance();
			IndexForFile iff = new IndexForFile(textsearch);
			iff.addIndex(file.getAbsolutePath().toString());
			// 서버 재시작
			exs.doTextSearchRestart(textsearch);
		}else{
			System.out.println("[Add Build] : File not exists !! ");
			throw new FileNotFoundException("File Not Found ~");
		}
	}
}
