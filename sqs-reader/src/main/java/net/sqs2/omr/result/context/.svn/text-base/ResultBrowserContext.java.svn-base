package net.sqs2.omr.result.context;

import java.util.List;
import java.util.Map;

import net.sqs2.omr.model.SourceDirectory;

public class ResultBrowserContext {
	private String pathInfo;
	private ResultBrowserParam resultBrowserParam;
	private UserAgentFeatureChecker userAgentFeatureChecker; 
		
	String title;
	List<SourceDirectory> selectedSourceDirectoryList;
	
	public ResultBrowserContext(String pathInfo, Map<String,String> paramMap, String userAgentName){
		this.pathInfo = pathInfo;
		this.resultBrowserParam = new ResultBrowserParam(paramMap);
		this.userAgentFeatureChecker = new UserAgentFeatureChecker(userAgentName);
	}
	
	public String getPathInfo(){
		return pathInfo;
	}
	
	public ResultBrowserParam getResultBrowserParam(){
		return this.resultBrowserParam;
	}
	
	public UserAgentFeatureChecker getUserAgentFeatureChecker(){
		return this.userAgentFeatureChecker;
	}
	
}
