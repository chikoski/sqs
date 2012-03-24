package net.sqs2.omr.app;

import net.sqs2.util.Resource;

public class App {
    public static final String SKIN_ID = "smp";
	//public static final String SKIN_ID = "sqs";
	static Resource appResource = new Resource("app-"+SKIN_ID);
	
	public static final String BUILD_NAME = appResource.getString("build.name");
    public static final String COPYRIGHT_NOTICE = appResource.getString("copyright.notice");
    public static final String GROUP_ID = appResource.getString("group.id");
    
    public static final String APP_NAME = appResource.getString("app.name");
    public static final String APP_ID = appResource.getString("app.id");
    public static final String BUILD_ID = appResource.getString("build.id");
    
    public static final String MAIN_ICON = appResource.getString("main.icon");
    public static final String SUB_ICON = appResource.getString("sub.icon");
    
	public static final String[] RESULT_DIRNAMES = appResource.getString("result.dirnames").split(",");
	
	public static final String RELEASE_ID = APP_ID+"_"+BUILD_ID;
	public static final int HTTP_PORT = 6970;

	public static String resultDirectoryName = RESULT_DIRNAMES[0];
	
	public static void setResultDirectoryName(String value){
		resultDirectoryName = value;
	}

	public static String getResultDirectoryName(){
		return resultDirectoryName;
	}

}
