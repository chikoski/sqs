package net.sqs2.omr.result.export;

import java.io.File;
import java.io.IOException;

import net.sqs2.omr.app.MarkReaderConstants;

public class CSSFileExportModule{

    protected File orgin;

    public CSSFileExportModule(){
	this.origin = new File(MarkReaderConstants.USER_CUSTOMIZE_CONSTANTS_DIR + File.separatorChar + "css");
    }

}
