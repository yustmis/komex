package com.list.lod.collector;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;
import com.list.lod.util.ResourceBundle;

public class FetchFactory implements TripleWriter{
	
	private String type = "";
	private String domainName = "";
	private List<String> prop = null;
	private Model _model = ModelFactory.createDefaultModel();
	private String outputFile = null;
	private boolean fetchFlag = false;
	
	public String getOutputFile() {
		return outputFile;
	}
	
	public void setOutputFile(String outputFile) {
		this.outputFile = outputFile;
	}

	public boolean isFetchFlag() {
		return fetchFlag;
	}

	public void setFetchFlag(boolean fetchFlag) {
		this.fetchFlag = fetchFlag;
	}
	

	public void fetch(String ouri, InfoWorker worker){
		if(worker == null){
			throw new NullPointerException("Worker is null !!!! ");
		}else{
			this.type = worker.getType();
			this.domainName = worker.getDomainName();
			this.prop = worker.getPropList();
		}
		
		String uri = encode(ouri);
		BufferedReader rd = null;
		try {
			//send data
			URL resourceUrl = new URL(uri.trim());
			HttpURLConnection conn = (HttpURLConnection)resourceUrl.openConnection();
			
			//try POST -- if set false, make call by using get method
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestMethod("GET");
			
			//get Response
			rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			String line;
			String retchString ="" ; 
			while((line = rd.readLine())!= null){
				retchString += line ;
			}
			
			Model m = ModelFactory.createDefaultModel();
			Model model = m.read(new ByteArrayInputStream(retchString.getBytes()), null, "TURTLE");
			StmtIterator listStatements = model.listStatements();
			Resource subject = null;
			Statement next = null;
			Property property = null;
			
			if(fetchFlag){
				_model.removeAll(); // 먼저 다 클리어 해놓는다. 
			}
			
			while(listStatements.hasNext()){
				next = listStatements.next();
				subject = next.getSubject();
				property = next.getPredicate();
				
				if(!subject.getURI().equals(uri)&&prop.contains(property.getURI())){
					if(property.equals(RDF.type)){
						_model.add(next);
					}else{
						Statement statement = ResourceFactory.createStatement(next.getSubject(), RDFS.label, next.getObject());
						_model.add(statement);
					}
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				if(rd != null) rd.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	@Override
	public void writeResultToFile(){
		FileWriter out = null;
		try {
			if(outputFile == null){
				outputFile = createName(System.currentTimeMillis());
			}
			//String fileName = createName(System.currentTimeMillis());
			out = new FileWriter(ResourceBundle.fileBundle.getString("output_file_path")+"/"+outputFile+".ttl", true);
			_model.write(out, "TURTLE");
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				fetchFlag = false;
				_model.removeAll(); // 파일에 다 적은 뒤에 당연히 모델 초기화 하는 것임. 
				if(out != null) out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private String createName(long dateTaken){
        Date date = new Date(dateTaken);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        return dateFormat.format(date);
    }
	
	
	private String encode(String url){
		//resources 인 경우 --> 한국사 lod
		if(url.contains("resources")){ 	
			//do nothing
		}else if(url.contains("lod/id")){ //수자원 공사
			url = url.replaceFirst("lod/id", "lod/data/id");
		}else{  //기타 resource 로 정의된 uri
			url = url.replaceFirst("resource", "data");
		}
		
		int j = url.lastIndexOf("/");
		String _url = url.substring(0, j+1);
		
		if(url.contains("data")){
			
			String localname = url.substring(j+1, url.length());
			try {
				
				if(InsModule.domainList.contains(domainName)) { // LOD --> output format
					localname = URLEncoder.encode(localname,"UTF-8")+"?output="+type;
					_url += localname;
//					System.out.println(_url);
				}else{                                              // LOD --> Etc.
					localname = URLEncoder.encode(localname,"UTF-8")+"."+type;
					_url += localname;
//					System.out.println(_url);
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			
			return _url;
		}else{
			if(url.contains("resources")){
				_url += "data/";
				String localname = url.substring(j+1, url.length());
				try {
					localname = URLEncoder.encode(localname,"UTF-8")+"?output=ttl";
					_url += localname;
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				return _url;
				
			}else{
				return url;
			}
			
		}
	}
}
