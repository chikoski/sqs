package net.sqs2.omr.result.export;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;

import net.sqs2.omr.app.MarkReaderConstants;

public class CSSFileExportModule{

    protected File origin;

    public CSSFileExportModule(){
	this.origin = new File(MarkReaderConstants.USER_CUSTOMIZE_CONSTANTS_DIR, "css");
    }

    public boolean export(File resultDirectoryFile) throws IOException{
	File cssDirectoryFile = new File(resultDirectoryFile, "css"); // XXX
	System.err.println(cssDirectoryFile);
	return true;
    }

}
