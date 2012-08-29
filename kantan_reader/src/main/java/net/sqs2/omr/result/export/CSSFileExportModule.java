package net.sqs2.omr.result.export;

import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import java.io.FileOutputStream;

import net.sqs2.omr.app.MarkReaderConstants;

public class CSSFileExportModule{

    protected ClassLoader loader;

    public CSSFileExportModule(){
	this.loader = getClass().getClassLoader();
    }

    public boolean export(File resultDirectoryFile) throws IOException{
	File cssDirectoryFile = createCSSExportDirectory(resultDirectoryFile);
	copyCSSFile("base.css", cssDirectoryFile); // XXX 
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
	    copyInputStreamToFile(in, dest);
	}
	return true;
    }

    protected void copyInputStreamToFile(InputStream in, File dest) throws IOException{ 
	FileOutputStream out = new FileOutputStream(dest);
	int bufferSize = 1024 * 4;
	byte[] buffer = new byte[bufferSize];
	int readSize = -1;
	while((readSize = in.read(buffer)) != -1){
	    out.write(buffer, 0, readSize);
	}
	in.close();
	out.close();
    } 

}
