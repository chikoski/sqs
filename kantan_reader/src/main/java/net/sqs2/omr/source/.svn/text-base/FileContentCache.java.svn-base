/**
 * 
 */
package net.sqs2.omr.source;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import net.sqs2.omr.util.FileResource;
import net.sqs2.util.FileResourceID;
import net.sqs2.util.FileUtil;

public class FileContentCache extends ResourceCache<FileResource>{
	
	File rootDirectory;
	
	public FileContentCache(File rootDirectory, int cacheSize){
		super(cacheSize);
		this.rootDirectory = rootDirectory;
	}
	
	public FileResource get(FileResourceID fileResourceID)throws IOException{
		FileResource fileResource;
		synchronized(this.map){
			fileResource = this.map.get(fileResourceID);
			if (fileResource == null) {
				byte[] bytes = createByteArray(fileResourceID.getRelativePath());
				long lastModified = getLastModified(fileResourceID.getRelativePath());
				fileResource = new FileResource(bytes, lastModified);
				this.map.put(fileResourceID, fileResource);
			}
		}
		return fileResource;
	}
	
	public long getLastModified(String relativePath) {
		File file = new File(this.rootDirectory.getAbsolutePath(), relativePath);
		return file.lastModified();
	}

	private byte[] createByteArray(String relativePath) throws IOException{
		BufferedInputStream in = null;
		ByteArrayOutputStream out = null;
		try{
			File file = new File(this.rootDirectory, relativePath);
			in = new BufferedInputStream(new FileInputStream(file));
			out = new ByteArrayOutputStream();
			FileUtil.connect(in, out);
		}finally{
			if(in != null){
				in.close();
			}
			if(out != null){
				out.close();
			}
		}
		return out.toByteArray();
	}
}