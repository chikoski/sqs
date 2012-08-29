package net.sqs2.omr.result.export;

import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import org.apache.commons.io.FileUtils;

import net.sqs2.omr.app.MarkReaderConstants;

public class CSSFileExportModule{

    protected File origin;
    protected ClassLoader loader;

    public CSSFileExportModule(){
	this.origin = new File(MarkReaderConstants.USER_CUSTOMIZE_CONSTANTS_DIR, "css");
	this.loader = getClass().getClassLoader();
    }

    public boolean export(File resultDirectoryFile) throws IOException{
	File cssDirectoryFile = createCSSExportDirectory(resultDirectoryFile);
	copyCSSFile("base.css", cssDirectoryFile);
	//	FileUtils.copyDirectory(this.origin, cssDirectoryFile);
	return true;
    }

    protected File createCSSExportDirectory(File resultDirectoryFile) throws IOException{
	File cssDirectoryFile = new File(resultDirectoryFile, "css"); // XXX
	cssDirectoryFile.mkdirs();
	return cssDirectoryFile;
    }

    protected boolean copyCSSFile(String filename, File cssDirectoryFile) throws IOException{
	File dest = new File(cssDirectoryFile, filename);
	InputStream in = this.loader.getResourceAsStream("css/" + filename);
	if(in != null){
	    FileUtils.copyInputStreamToFile(in, dest);
	}
	return true;
    }

}
