package net.sqs2.omr.result.context;

public class UserAgentFeatureChecker {
	
	boolean isBase64ImageSrcSupported = false;
	boolean isBackgroundImageOfOptionElementSupported = false;
	
	public UserAgentFeatureChecker(boolean isBase64ImageSrcSupported,
			boolean isBackgroundImageOfOptionElementSupported){
		this.isBase64ImageSrcSupported = isBase64ImageSrcSupported;
		this.isBackgroundImageOfOptionElementSupported = isBackgroundImageOfOptionElementSupported;
	}
	
	public UserAgentFeatureChecker(String userAgentName){
		if(0 <= userAgentName.indexOf("MSIE")){
			this.isBase64ImageSrcSupported = false;
			this.isBackgroundImageOfOptionElementSupported = false; 
		}else{
			this.isBase64ImageSrcSupported = true;
			this.isBackgroundImageOfOptionElementSupported = true; 
		}
	}

	public boolean isBase64ImageSrcSupported() {
		return isBase64ImageSrcSupported;
	}

	public boolean isBackgroundImageOfOptionElementSupported() {
		return isBackgroundImageOfOptionElementSupported;
	}
	
}
